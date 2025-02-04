package io.devarium.api.controller.feedback.dto;

import io.devarium.core.domain.feedback.question.QuestionContent;
import io.devarium.core.domain.feedback.question.QuestionType;
import io.devarium.core.domain.feedback.question.port.CreateQuestion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateQuestionRequest(
    @Positive(message = "'orderNumber' must be greater than 0") int orderNumber,
    @NotBlank(message = "'content' must not be blank") QuestionContent content,
    @NotNull(message = "'type' must not be null") QuestionType type,
    @NotNull(message = "'required' must not be null") Boolean required
) implements CreateQuestion {

}
