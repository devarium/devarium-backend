package io.devarium.core.domain.feedback.service;

import io.devarium.core.domain.feedback.Feedback;
import io.devarium.core.domain.feedback.FeedbackSummary;
import io.devarium.core.domain.feedback.answer.Answer;
import io.devarium.core.domain.feedback.answer.port.SubmitAnswers;
import io.devarium.core.domain.feedback.question.Question;
import io.devarium.core.domain.feedback.question.port.SyncQuestions;
import io.devarium.core.domain.user.User;
import java.util.List;

public interface FeedbackService {

    List<Answer> submitFeedbackAnswers(Long projectId, SubmitAnswers request, User user);

    List<Feedback> getFeedbacks(Long projectId, User user);

    List<FeedbackSummary> getFeedbackSummaries(Long projectId, User user);

    List<Question> getFeedbackQuestions(Long projectId);

    List<Question> syncFeedbackQuestions(Long projectId, SyncQuestions request, User user);
}
