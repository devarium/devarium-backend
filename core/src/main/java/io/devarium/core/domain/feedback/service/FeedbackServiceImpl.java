package io.devarium.core.domain.feedback.service;

import io.devarium.core.domain.feedback.answer.repository.AnswerRepository;
import io.devarium.core.domain.feedback.question.Question;
import io.devarium.core.domain.feedback.question.port.CreateQuestions;
import io.devarium.core.domain.feedback.question.repository.QuestionRepository;
import io.devarium.core.domain.user.User;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @Override
    public List<Question> createFeedbackQuestions(
        Long projectId,
        CreateQuestions request,
        User user
    ) {
        return null;
    }
}
