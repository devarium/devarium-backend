package io.devarium.core.domain.feedback.service;

import io.devarium.core.domain.feedback.answer.repository.AnswerRepository;
import io.devarium.core.domain.feedback.question.Question;
import io.devarium.core.domain.feedback.question.port.CreateQuestions;
import io.devarium.core.domain.feedback.question.repository.QuestionRepository;
import io.devarium.core.domain.project.Project;
import io.devarium.core.domain.project.service.ProjectService;
import io.devarium.core.domain.user.User;
import java.util.List;
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
        Project project = projectService.getProject(projectId, user);
        // TODO: 프로젝트 생성 권한 검증

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
}
