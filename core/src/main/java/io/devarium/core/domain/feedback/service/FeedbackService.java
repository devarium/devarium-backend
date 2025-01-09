package io.devarium.core.domain.feedback.service;

import io.devarium.core.domain.feedback.question.FeedbackQuestion;
import io.devarium.core.domain.feedback.question.port.CreateFeedbackQuestions;
import io.devarium.core.domain.user.User;
import java.util.List;

public interface FeedbackService {

    List<FeedbackQuestion> createFeedbackQuestions(
        Long projectId,
        CreateFeedbackQuestions request,
        User user
    );
}
