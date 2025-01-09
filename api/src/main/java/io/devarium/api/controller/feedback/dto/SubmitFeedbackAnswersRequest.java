package io.devarium.api.controller.feedback.dto;

import io.devarium.core.domain.feedback.answer.Rating;
import io.devarium.core.domain.feedback.answer.port.SubmitFeedbackAnswer;
import io.devarium.core.domain.feedback.answer.port.SubmitFeedbackAnswers;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record SubmitFeedbackAnswersRequest(
    @NotNull(message = "'answers' must not be null") List<SubmitFeedbackAnswerRequest> answers
) implements SubmitFeedbackAnswers {

    public record SubmitFeedbackAnswerRequest(
        @NotNull(message = "'questionId' must not be null") Long questionId,
        String content,
        Rating rating
    ) implements SubmitFeedbackAnswer {

    }
}
