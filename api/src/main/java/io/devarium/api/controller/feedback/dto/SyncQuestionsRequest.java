package io.devarium.api.controller.feedback.dto;

import io.devarium.core.domain.feedback.question.QuestionType;
import io.devarium.core.domain.feedback.question.port.SyncQuestion;
import io.devarium.core.domain.feedback.question.port.SyncQuestions;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public record SyncQuestionsRequest(
    @NotNull(message = "'questions' must not be null") List<SyncQuestionRequest> questions
) implements SyncQuestions {

    public record SyncQuestionRequest(
        @NotNull(message = "'questionId' must not be null") Long questionId,
        @Positive(message = "'orderNumber' must be greater than 0") int orderNumber,
        @NotBlank(message = "'content' must not be blank") String content,
        @NotNull(message = "'type' must not be null") QuestionType type,
        @NotNull(message = "'required' must not be null") Boolean required
    ) implements SyncQuestion {

    }
}
