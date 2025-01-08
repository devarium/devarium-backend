package io.devarium.infrastructure.persistence.service;

import io.devarium.core.domain.feedback.service.FeedbackService;
import io.devarium.core.domain.feedback.service.FeedbackServiceImpl;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FeedbackServiceDecorator implements FeedbackService {

    private final FeedbackServiceImpl feedbackService;
}
