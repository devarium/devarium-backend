package io.devarium.core.domain.feedback.service;

import io.devarium.core.domain.feedback.Feedback;
import io.devarium.core.domain.feedback.QuestionWithAnswers;
import io.devarium.core.domain.feedback.answer.Answer;
import io.devarium.core.domain.feedback.answer.port.SubmitAnswers;
import io.devarium.core.domain.feedback.answer.repository.AnswerRepository;
import io.devarium.core.domain.feedback.exception.FeedbackErrorCode;
import io.devarium.core.domain.feedback.exception.FeedbackException;
import io.devarium.core.domain.feedback.question.Question;
import io.devarium.core.domain.feedback.question.port.CreateQuestions;
import io.devarium.core.domain.feedback.question.port.UpdateQuestions;
import io.devarium.core.domain.feedback.question.repository.QuestionRepository;
import io.devarium.core.domain.project.Project;
import io.devarium.core.domain.project.service.ProjectService;
import io.devarium.core.domain.user.User;
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

    @Override
    public List<Question> createFeedbackQuestions(
        Long projectId,
        CreateQuestions request,
        User user
    ) {
        Project project = projectService.getProject(projectId);
        // TODO: 프로젝트 접근 권환 확인

        List<Question> questions = request.questions().stream()
            .map(q -> Question.builder()
                .orderNumber(q.orderNumber())
                .content(q.content())
                .type(q.type())
                .required(q.required())
                .projectId(projectId)
                .build())
            .toList();

        return questionRepository.saveAll(questions);
    }

    @Override
    public List<Answer> submitFeedbackAnswers(Long projectId, SubmitAnswers request, User user) {
        Project project = projectService.getProject(projectId);
        // TODO: 리뷰 요청 중인 프로젝트인지 검증 필요

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
    public Feedback getFeedback(Long projectId, User user) {
        Project project = projectService.getProject(projectId);
        // TODO: 프로젝트 접근 권환 확인

        List<Question> questions = questionRepository.findAllByProjectId(projectId);
        List<Answer> answers = answerRepository.findAllByQuestionIdIn(
            questions.stream().map(Question::getId).toList()
        );
        Map<Long, List<Answer>> answersByQuestionId = answers.stream()
            .collect(Collectors.groupingBy(Answer::getQuestionId));
        List<QuestionWithAnswers> questionAnswers = questions.stream()
            .map(q -> new QuestionWithAnswers(
                q,
                answersByQuestionId.getOrDefault(q.getId(), List.of())
            ))
            .toList();

        return Feedback.of(projectId, questionAnswers);
    }

    @Override
    public List<Question> getFeedbackQuestions(Long projectId) {
        return questionRepository.findAllByProjectId(projectId);
    }

    @Override
    public List<Question> updateFeedbackQuestions(
        Long projectId,
        UpdateQuestions request,
        User user
    ) {
        Project project = projectService.getProject(projectId);
        // TODO: 프로젝트 접근 권환 확인

        Map<Long, Question> questionById = questionRepository.findAllByProjectId(projectId)
            .stream()
            .collect(Collectors.toMap(Question::getId, q -> q));

        List<Question> questions = request.questions().stream()
            .map(updateQuestion -> {
                Question question = Optional.ofNullable(
                    questionById.get(updateQuestion.questionId())
                ).orElseThrow(() -> new FeedbackException(
                    FeedbackErrorCode.QUESTION_NOT_FOUND,
                    updateQuestion.questionId())
                );

                question.update(
                    updateQuestion.orderNumber(),
                    updateQuestion.content(),
                    updateQuestion.required()
                );

                return question;
            })
            .toList();

        return questionRepository.saveAll(questions);
    }
}
