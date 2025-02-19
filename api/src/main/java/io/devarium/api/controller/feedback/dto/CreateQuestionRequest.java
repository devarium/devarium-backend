package io.devarium.api.controller.feedback.dto;

import io.devarium.api.controller.feedback.dto.CreateQuestionRequest.CreateQuestionContentRequest.CreateChoiceRequest;
import io.devarium.core.domain.feedback.question.QuestionContent;
import io.devarium.core.domain.feedback.question.QuestionType;
import io.devarium.core.domain.feedback.question.command.CreateQuestion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public record CreateQuestionRequest(
    @Positive(message = "'orderNumber' must be greater than 0") int orderNumber,
    @NotNull(message = "'createQuestionContent' must not be null") CreateQuestionContentRequest createQuestionContent,
    @NotNull(message = "'type' must not be null") QuestionType type,
    @NotNull(message = "'required' must not be null") Boolean required
) implements CreateQuestion {

    @Override
    public QuestionContent questionContent() {
        List<CreateChoiceRequest> choices = createQuestionContent.choices();

        return new QuestionContent(
            createQuestionContent.content(),
            choices != null ?
                choices.stream()
                    .map(choice -> new QuestionContent.Choice(
                        choice.orderNumber(),
                        choice.label()
                    ))
                    .toList() :
                List.of()
        );
    }

    public record CreateQuestionContentRequest(
        @NotBlank(message = "'questionContent' must not be blank") String content,

        List<CreateChoiceRequest> choices
    ) implements CreateQuestionContent {

        public record CreateChoiceRequest(
            @Positive(message = "'orderNumber' must be greater than 0") int orderNumber,

            @NotBlank(message = "'label' must not be blank") String label
        ) implements CreateChoice {

        }
    }
}
