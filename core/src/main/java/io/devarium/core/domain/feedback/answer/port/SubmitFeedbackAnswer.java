package io.devarium.core.domain.feedback.answer.port;

import io.devarium.core.domain.feedback.answer.Rating;

public interface SubmitFeedbackAnswer {

    Long questionId();

    String content();

    Rating rating();
}
