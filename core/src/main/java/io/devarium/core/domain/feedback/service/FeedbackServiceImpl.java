package io.devarium.core.domain.feedback.service;

import io.devarium.core.domain.feedback.Feedback;
import io.devarium.core.domain.feedback.FeedbackSummary;
import io.devarium.core.domain.feedback.answer.Answer;
import io.devarium.core.domain.feedback.answer.port.SubmitAnswers;
import io.devarium.core.domain.feedback.answer.port.SubmitAnswers.SubmitAnswer;
import io.devarium.core.domain.feedback.answer.repository.AnswerRepository;
import io.devarium.core.domain.feedback.exception.FeedbackErrorCode;
import io.devarium.core.domain.feedback.exception.FeedbackException;
import io.devarium.core.domain.feedback.port.TextSummarizer;
import io.devarium.core.domain.feedback.question.Question;
import io.devarium.core.domain.feedback.question.port.CreateQuestion;
import io.devarium.core.domain.feedback.question.port.UpdateQuestion;
import io.devarium.core.domain.feedback.question.port.UpdateQuestionOrders;
import io.devarium.core.domain.feedback.question.port.UpdateQuestionOrders.UpdateQuestionOrder;
import io.devarium.core.domain.feedback.question.repository.QuestionRepository;
import io.devarium.core.domain.member.Member;
import io.devarium.core.domain.member.service.MemberService;
import io.devarium.core.domain.project.Project;
import io.devarium.core.domain.project.service.ProjectService;
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
    private final MemberService memberService;
    private final TextSummarizer textSummarizer;

    @Override
    public Question createFeedbackQuestion(Long projectId, CreateQuestion request, User user) {
        Project project = projectService.getProject(projectId);
        Member member = memberService.getUserMembership(project.getTeamId(), user.getId());
        member.validateMembership(project.getTeamId());

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
    public Question updateFeedbackQuestion(
        Long projectId,
        Long questionId,
        UpdateQuestion request,
        User user
    ) {
        Project project = projectService.getProject(projectId);
        Member member = memberService.getUserMembership(project.getTeamId(), user.getId());
        member.validateMembership(project.getTeamId());

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

    @Override
    public List<Question> updateFeedbackQuestionOrders(
        Long projectId,
        UpdateQuestionOrders request,
        User user
    ) {
        Project project = projectService.getProject(projectId);
        Member member = memberService.getUserMembership(project.getTeamId(), user.getId());
        member.validateMembership(project.getTeamId());

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
        Member member = memberService.getUserMembership(project.getTeamId(), user.getId());
        member.validateMembership(project.getTeamId());

        questionRepository.deleteById(questionId);
    }
}
