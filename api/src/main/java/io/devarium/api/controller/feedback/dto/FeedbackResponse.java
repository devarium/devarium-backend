package io.devarium.api.controller.feedback.dto;

import io.devarium.core.domain.feedback.Feedback;

public record FeedbackResponse() {

    public static FeedbackResponse from(Feedback feedback) {
        return new FeedbackResponse();
    }
}
