package io.devarium.core.domain.feedback.service;

import io.devarium.core.domain.feedback.answer.Answer;
import io.devarium.core.domain.feedback.answer.port.SubmitAnswers;
import io.devarium.core.domain.feedback.question.Question;
import io.devarium.core.domain.feedback.question.port.CreateQuestions;
import io.devarium.core.domain.user.User;
import java.util.List;

public interface FeedbackService {

    List<Question> createFeedbackQuestions(
        Long projectId,
        CreateQuestions request,
        User user
    );

    List<Answer> submitFeedbackAnswers(Long projectId, SubmitAnswers request, User user);

    List<Question> getFeedbackQuestions(Long projectId);

    List<Answer> getFeedbackAnswers(Long projectId, User user);
}
