package io.devarium.api.controller.feedback.dto;

import io.devarium.core.domain.feedback.answer.Rating;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record SubmitFeedbackAnswersRequest(
    @NotNull(message = "'answers' must not be null") List<SubmitFeedbackAnswerRequest> answers
) {

    public record SubmitFeedbackAnswerRequest(
        @NotNull(message = "'questionId' must not be null") Long questionId,
        String content,
        Rating rating
    ) {

    }
}
