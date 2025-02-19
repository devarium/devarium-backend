package io.devarium.core.domain.feedback.service;

import io.devarium.core.domain.feedback.Feedback;
import io.devarium.core.domain.feedback.FeedbackSummary;
import io.devarium.core.domain.feedback.answer.Answer;
import io.devarium.core.domain.feedback.answer.command.SubmitAnswers;
import io.devarium.core.domain.feedback.answer.command.SubmitAnswers.SubmitAnswer;
import io.devarium.core.domain.feedback.exception.FeedbackErrorCode;
import io.devarium.core.domain.feedback.exception.FeedbackException;
import io.devarium.core.domain.feedback.port.in.FeedbackService;
import io.devarium.core.domain.feedback.port.out.AnswerRepository;
import io.devarium.core.domain.feedback.port.out.QuestionRepository;
import io.devarium.core.domain.feedback.port.out.TextSummarizer;
import io.devarium.core.domain.feedback.question.Question;
import io.devarium.core.domain.feedback.question.command.CreateQuestion;
import io.devarium.core.domain.feedback.question.command.UpdateQuestion;
import io.devarium.core.domain.feedback.question.command.UpdateQuestionOrders;
import io.devarium.core.domain.feedback.question.command.UpdateQuestionOrders.UpdateQuestionOrder;
import io.devarium.core.domain.membership.port.in.MembershipService;
import io.devarium.core.domain.project.Project;
import io.devarium.core.domain.project.port.in.ProjectService;
import io.devarium.core.domain.user.User;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final ProjectService projectService;
    private final MembershipService membershipService;
    private final TextSummarizer textSummarizer;

    @Override
    public Question createFeedbackQuestion(Long projectId, CreateQuestion request, User user) {
        Project project = projectService.getProject(projectId);
        validateMembership(project.getTeamId(), user.getId());

        questionRepository.incrementOrderNumbersFrom(projectId, request.orderNumber());

        Question question = Question.builder()
            .orderNumber(request.orderNumber())
            .questionContent(request.questionContent())
            .type(request.type())
            .required(request.required())
            .projectId(projectId)
            .build();

        return questionRepository.save(question);
    }

    @Override
    public List<Answer> submitFeedbackAnswers(Long projectId, SubmitAnswers request, User user) {
        Project project = projectService.getProject(projectId);
        project.validateStatusInReview();

        Map<Long, Answer> answerById = answerRepository.findAllByUserIdAndQuestionIdIn(
                user.getId(),
                request.answers().stream().map(SubmitAnswer::questionId).toList()
            )
            .stream()
            .collect(Collectors.toMap(Answer::getQuestionId, a -> a));

        Map<Long, Question> questionById = questionRepository.findAllByProjectId(projectId).stream()
            .collect(Collectors.toMap(Question::getId, q -> q));

        List<Answer> answers = request.answers().stream()
            .map(a -> {
                Question question = Optional.ofNullable(questionById.get(a.questionId()))
                    .orElseThrow(() -> new FeedbackException(
                        FeedbackErrorCode.QUESTION_NOT_FOUND,
                        a.questionId()
                    ));

                return Optional.ofNullable(answerById.get(question.getId()))
                    .map(answer -> {
                        answer.updateContent(a.content());
                        return answer;
                    })
                    .orElse(
                        Answer.builder()
                            .content(a.content())
                            .questionId(question.getId())
                            .userId(user.getId())
                            .build()
                    );
            })
            .toList();

        return answerRepository.saveAll(answers);
    }

    @Override
    public List<Feedback> getFeedbacks(Long projectId, User user) {
        Project project = projectService.getProject(projectId);
        validateMembership(project.getTeamId(), user.getId());

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
    public Question updateFeedbackQuestion(
        Long projectId,
        Long questionId,
        UpdateQuestion request,
        User user
    ) {
        Project project = projectService.getProject(projectId);
        validateMembership(project.getTeamId(), user.getId());

        Question question = questionRepository.findById(questionId)
            .orElseThrow(() ->
                new FeedbackException(FeedbackErrorCode.QUESTION_NOT_FOUND, questionId)
            );
        question.validateProjectAccess(projectId);
        question.update(
            request.content(),
            request.type(),
            request.required()
        );

        return questionRepository.save(question);
    }

    @Override
    public List<Question> updateFeedbackQuestionOrders(
        Long projectId,
        UpdateQuestionOrders request,
        User user
    ) {
        Project project = projectService.getProject(projectId);
        validateMembership(project.getTeamId(), user.getId());

        List<Question> questions = questionRepository.findAllByProjectId(projectId);

        Map<Long, Integer> orderNumberById = request.orderNumbers().stream()
            .collect(Collectors.toMap(
                UpdateQuestionOrder::questionId,
                UpdateQuestionOrder::orderNumber)
            );

        questions.forEach(question ->
            question.updateOrderNumber(orderNumberById.get(question.getId()))
        );

        return questionRepository.saveAll(questions);
    }

    @Override
    public void deleteFeedbackQuestion(Long projectId, Long questionId, User user) {
        Project project = projectService.getProject(projectId);
        validateMembership(project.getTeamId(), user.getId());

        questionRepository.deleteById(questionId);
    }

    private void validateMembership(Long teamId, Long userId) {
        if (!membershipService.checkMembershipExists(teamId, userId)) {
            throw new FeedbackException(
                FeedbackErrorCode.MEMBERSHIP_NOT_FOUND,
                teamId,
                userId
            );
        }
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
}
