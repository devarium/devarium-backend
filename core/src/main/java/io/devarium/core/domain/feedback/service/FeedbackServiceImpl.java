package io.devarium.core.domain.feedback.service;

import io.devarium.core.domain.feedback.answer.repository.FeedbackAnswerRepository;
import io.devarium.core.domain.feedback.question.repository.FeedbackQuestionRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackQuestionRepository feedbackQuestionRepository;
    private final FeedbackAnswerRepository feedbackAnswerRepository;
}
