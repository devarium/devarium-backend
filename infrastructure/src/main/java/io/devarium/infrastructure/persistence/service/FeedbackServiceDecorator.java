package io.devarium.infrastructure.persistence.service;

import io.devarium.core.domain.feedback.Feedback;
import io.devarium.core.domain.feedback.answer.Answer;
import io.devarium.core.domain.feedback.answer.port.SubmitAnswers;
import io.devarium.core.domain.feedback.question.Question;
import io.devarium.core.domain.feedback.question.port.SyncQuestions;
import io.devarium.core.domain.feedback.service.FeedbackService;
import io.devarium.core.domain.feedback.service.FeedbackServiceImpl;
import io.devarium.core.domain.user.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class FeedbackServiceDecorator implements FeedbackService {

    private final FeedbackServiceImpl feedbackService;

    @Override
    @Transactional
    public List<Answer> submitFeedbackAnswers(Long projectId, SubmitAnswers request, User user) {
        return feedbackService.submitFeedbackAnswers(projectId, request, user);
    }

    @Override
    @Transactional(readOnly = true)
    public Feedback getFeedback(Long projectId, User user) {
        return feedbackService.getFeedback(projectId, user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Question> getFeedbackQuestions(Long projectId) {
        return feedbackService.getFeedbackQuestions(projectId);
    }

    @Override
    @Transactional
    public List<Question> syncFeedbackQuestions(Long projectId, SyncQuestions request, User user) {
        return feedbackService.syncFeedbackQuestions(projectId, request, user);
    }
}
