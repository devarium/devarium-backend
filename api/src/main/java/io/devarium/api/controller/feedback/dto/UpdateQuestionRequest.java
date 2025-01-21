package io.devarium.api.controller.feedback.dto;

import io.devarium.core.domain.feedback.question.QuestionType;
import io.devarium.core.domain.feedback.question.port.UpdateQuestion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateQuestionRequest(
    @NotBlank(message = "'content' must not be blank") String content,
    @NotNull(message = "'type' must not be null") QuestionType type,
    @NotNull(message = "'required' must not be null") Boolean required
) implements UpdateQuestion {

}
