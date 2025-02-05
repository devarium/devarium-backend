package io.devarium.core.domain.feedback.service;

import io.devarium.core.domain.feedback.Feedback;
import io.devarium.core.domain.feedback.FeedbackSummary;
import io.devarium.core.domain.feedback.answer.Answer;
import io.devarium.core.domain.feedback.answer.port.SubmitAnswers;
import io.devarium.core.domain.feedback.question.Question;
import io.devarium.core.domain.feedback.question.port.CreateQuestion;
import io.devarium.core.domain.feedback.question.port.UpdateQuestion;
import io.devarium.core.domain.feedback.question.port.UpdateQuestionOrders;
import io.devarium.core.domain.user.User;
import java.util.List;

public interface FeedbackService {

    Question createFeedbackQuestion(Long projectId, CreateQuestion request, User user);

    List<Answer> submitFeedbackAnswers(Long projectId, SubmitAnswers request, User user);

    List<Feedback> getFeedbacks(Long projectId, User user);

    List<FeedbackSummary> getFeedbackSummaries(Long projectId, User user);

    List<Question> getFeedbackQuestions(Long projectId);

    Question updateFeedbackQuestion(
        Long projectId,
        Long questionId,
        UpdateQuestion request,
        User user
    );

    List<Question> updateFeedbackQuestionOrders(
        Long projectId,
        UpdateQuestionOrders request,
        User user
    );

    void deleteFeedbackQuestion(Long projectId, Long questionId, User user);
}
