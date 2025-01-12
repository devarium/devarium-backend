package io.devarium.api.controller.feedback.dto;

import io.devarium.core.domain.feedback.question.QuestionType;
import io.devarium.core.domain.feedback.question.port.CreateQuestion;
import io.devarium.core.domain.feedback.question.port.CreateQuestions;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.List;

public record CreateQuestionsRequest(
    @NotNull(message = "'questions' must not be null") List<CreateQuestionRequest> questions
) implements CreateQuestions {

    public record CreateQuestionRequest(
        @PositiveOrZero(message = "'orderNumber' must be greater than or equal to 0") int orderNumber,
        @NotBlank(message = "'content' must not be blank") String content,
        @NotNull(message = "'type' must not be null") QuestionType type,
        @NotNull(message = "'required' must not be null") Boolean required
    ) implements CreateQuestion {

    }
}
