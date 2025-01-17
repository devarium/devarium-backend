package io.devarium.core.domain.feedback.service;

import io.devarium.core.domain.feedback.Feedback;
import io.devarium.core.domain.feedback.FeedbackSummary;
import io.devarium.core.domain.feedback.answer.Answer;
import io.devarium.core.domain.feedback.answer.port.SubmitAnswers;
import io.devarium.core.domain.feedback.answer.repository.AnswerRepository;
import io.devarium.core.domain.feedback.exception.FeedbackErrorCode;
import io.devarium.core.domain.feedback.exception.FeedbackException;
import io.devarium.core.domain.feedback.port.TextSummarizer;
import io.devarium.core.domain.feedback.question.Question;
import io.devarium.core.domain.feedback.question.port.SyncQuestion;
import io.devarium.core.domain.feedback.question.port.SyncQuestions;
import io.devarium.core.domain.feedback.question.repository.QuestionRepository;
import io.devarium.core.domain.member.Member;
import io.devarium.core.domain.member.service.MemberService;
import io.devarium.core.domain.project.Project;
import io.devarium.core.domain.project.service.ProjectService;
import io.devarium.core.domain.user.User;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final ProjectService projectService;
    private final MemberService memberService;
    private final TextSummarizer textSummarizer;

    @Override
    public List<Answer> submitFeedbackAnswers(Long projectId, SubmitAnswers request, User user) {
        Project project = projectService.getProject(projectId);
        project.validateStatusInReview();

        Map<Long, Question> questionById = questionRepository.findAllByProjectId(projectId).stream()
            .collect(Collectors.toMap(Question::getId, q -> q));

        List<Answer> answers = request.answers().stream()
            .map(a -> {
                Question question = Optional.ofNullable(questionById.get(a.questionId()))
                    .orElseThrow(() -> new FeedbackException(
                        FeedbackErrorCode.QUESTION_NOT_FOUND,
                        a.questionId()
                    ));

                return Answer.builder()
                    .content(a.content())
                    .rating(a.rating())
                    .questionId(question.getId())
                    .userId(user.getId())
                    .build();
            })
            .toList();

        return answerRepository.saveAll(answers);
    }

    @Override
    public List<Feedback> getFeedbacks(Long projectId, User user) {
        Project project = projectService.getProject(projectId);
        Member member = memberService.getUserMembership(project.getTeamId(), user.getId());
        member.validateMembership(project.getTeamId());

        List<Question> questions = questionRepository.findAllByProjectId(projectId);
        List<Answer> answers = answerRepository.findAllByQuestionIdIn(
            questions.stream().map(Question::getId).toList()
        );
        Map<Long, List<Answer>> answersByQuestionId = answers.stream()
            .collect(Collectors.groupingBy(Answer::getQuestionId));

        return questions.stream()
            .sorted(Comparator.comparing(Question::getOrderNumber))
            .map(q -> Feedback.of(
                q,
                answersByQuestionId.getOrDefault(q.getId(), List.of())
            ))
            .toList();
    }

    @Override
    public List<FeedbackSummary> getFeedbackSummaries(Long projectId, User user) {
        List<Feedback> feedbacks = getFeedbacks(projectId, user);
        return feedbacks.stream()
            .map(this::summarizeFeedback)
            .toList();
    }

    @Override
    public List<Question> getFeedbackQuestions(Long projectId) {
        return questionRepository.findAllByProjectId(projectId);
    }

    @Override
    public List<Question> syncFeedbackQuestions(
        Long projectId,
        SyncQuestions request,
        User user
    ) {
        Project project = projectService.getProject(projectId);
        Member member = memberService.getUserMembership(project.getTeamId(), user.getId());
        member.validateMembership(project.getTeamId());

        // 기존 질문 조회
        List<Question> questions = questionRepository.findAllByProjectId(projectId);

        Set<Long> missingQuestionIds = getMissingQuestions(request, questions);
        Set<Long> typeChangedQuestionIds = getTypeChangedQuestions(request, questions);
        questionRepository.deleteAllById(missingQuestionIds);
        answerRepository.deleteAllByQuestionIdIn(
            Stream.concat(
                missingQuestionIds.stream(),
                typeChangedQuestionIds.stream()
            ).collect(Collectors.toSet())
        );

        return questionRepository.saveAll(upsertQuestions(projectId, request, questions));
    }


    private FeedbackSummary summarizeFeedback(Feedback feedback) {
        List<String> answerContents = feedback.answers().stream()
            .map(Answer::getContent)
            .toList();

        String summary = textSummarizer.summarizeTexts(answerContents);

        Answer summariedAnswer = Answer.builder()
            .content(summary)
            .questionId(feedback.question().getId())
            .build();

        return FeedbackSummary.of(feedback.question(), summariedAnswer);
    }

    private List<Question> upsertQuestions(
        Long projectId,
        SyncQuestions request,
        List<Question> questions
    ) {
        Map<Long, Question> questionById = questions.stream()
            .collect(Collectors.toMap(Question::getId, q -> q));

        return request.questions().stream()
            .map(syncQuestion ->
                syncQuestion.questionId() == null ?
                    createQuestion(projectId, syncQuestion) :
                    updateQuestion(syncQuestion, questionById.get(syncQuestion.questionId()))
            )
            .toList();
    }

    private Question createQuestion(Long projectId, SyncQuestion request) {
        return Question.builder()
            .orderNumber(request.orderNumber())
            .content(request.content())
            .type(request.type())
            .required(request.required())
            .projectId(projectId)
            .build();
    }

    private Question updateQuestion(SyncQuestion request, Question question) {
        if (question == null) {
            throw new FeedbackException(
                FeedbackErrorCode.QUESTION_NOT_FOUND,
                request.questionId()
            );
        }

        question.update(
            request.orderNumber(),
            request.content(),
            request.type(),
            request.required()
        );

        return question;
    }

    private Set<Long> getMissingQuestions(SyncQuestions request, List<Question> questions) {
        Set<Long> requestedIds = request.questions().stream()
            .map(SyncQuestion::questionId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

        return questions.stream()
            .map(Question::getId)
            .filter(id -> !requestedIds.contains(id))
            .collect(Collectors.toSet());
    }

    private Set<Long> getTypeChangedQuestions(SyncQuestions request, List<Question> questions) {
        Map<Long, Question> questionById = questions.stream()
            .collect(Collectors.toMap(Question::getId, q -> q));

        return request.questions().stream()
            .filter(q -> {
                Question existingQuestion = questionById.get(q.questionId());
                return existingQuestion != null && !existingQuestion.getType().equals(q.type());
            })
            .map(SyncQuestion::questionId)
            .collect(Collectors.toSet());
    }
}
