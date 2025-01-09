package io.devarium.infrastructure.persistence.service;

import io.devarium.core.domain.feedback.question.FeedbackQuestion;
import io.devarium.core.domain.feedback.question.port.CreateFeedbackQuestions;
import io.devarium.core.domain.feedback.service.FeedbackService;
import io.devarium.core.domain.feedback.service.FeedbackServiceImpl;
import io.devarium.core.domain.user.User;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FeedbackServiceDecorator implements FeedbackService {

    private final FeedbackServiceImpl feedbackService;

    @Override
    public List<FeedbackQuestion> createFeedbackQuestions(
        Long projectId,
        CreateFeedbackQuestions request,
        User user
    ) {
        return feedbackService.createFeedbackQuestions(projectId, request, user);
    }
}
