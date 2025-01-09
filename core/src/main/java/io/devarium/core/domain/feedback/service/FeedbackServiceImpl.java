package io.devarium.core.domain.feedback.service;

import io.devarium.core.domain.feedback.answer.repository.FeedbackAnswerRepository;
import io.devarium.core.domain.feedback.question.FeedbackQuestion;
import io.devarium.core.domain.feedback.question.port.CreateFeedbackQuestions;
import io.devarium.core.domain.feedback.question.repository.FeedbackQuestionRepository;
import io.devarium.core.domain.user.User;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackQuestionRepository feedbackQuestionRepository;
    private final FeedbackAnswerRepository feedbackAnswerRepository;

    @Override
    public List<FeedbackQuestion> createFeedbackQuestions(
        Long projectId,
        CreateFeedbackQuestions request,
        User user
    ) {
        return null;
    }
}
