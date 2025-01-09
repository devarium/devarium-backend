package io.devarium.api.controller.feedback.dto;

import io.devarium.core.domain.feedback.question.QuestionType;
import io.devarium.core.domain.feedback.question.port.UpdateQuestion;
import io.devarium.core.domain.feedback.question.port.UpdateQuestions;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.List;

public record UpdateQuestionsRequest(
    @NotNull(message = "'questions' must not be null") List<UpdateQuestionRequest> questions
) implements UpdateQuestions {

    public record UpdateQuestionRequest(
        @NotNull(message = "'questionId' must not be null") Long questionId,
        @NotBlank(message = "'content' must not be blank") String content,
        @NotNull(message = "'type' must not be null") QuestionType type,
        @NotNull(message = "'required' must not be null") Boolean required,
        @PositiveOrZero(message = "'orderNumber' must be greater than or equal to 0") int orderNumber
    ) implements UpdateQuestion {

    }
}
