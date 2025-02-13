package io.devarium.core.domain.feedback.port.in;

import io.devarium.core.domain.feedback.Feedback;
import io.devarium.core.domain.feedback.FeedbackSummary;
import io.devarium.core.domain.feedback.answer.Answer;
import io.devarium.core.domain.feedback.answer.command.SubmitAnswers;
import io.devarium.core.domain.feedback.question.Question;
import io.devarium.core.domain.feedback.question.command.CreateQuestion;
import io.devarium.core.domain.feedback.question.command.UpdateQuestion;
import io.devarium.core.domain.feedback.question.command.UpdateQuestionOrders;
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
