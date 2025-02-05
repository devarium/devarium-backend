package io.devarium.api.controller.feedback.dto;

import io.devarium.core.domain.feedback.answer.port.SubmitAnswers;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record SubmitAnswersRequest(
    @NotNull(message = "'answers' must not be null") List<SubmitAnswerRequest> answers
) implements SubmitAnswers {

    public record SubmitAnswerRequest(
        @NotNull(message = "'questionId' must not be null") Long questionId,
        String content
    ) implements SubmitAnswer {

    }
}
