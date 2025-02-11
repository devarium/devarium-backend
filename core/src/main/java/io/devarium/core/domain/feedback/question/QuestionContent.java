package io.devarium.core.domain.feedback.question;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

public record QuestionContent(
    String content,
    @JsonInclude(JsonInclude.Include.NON_NULL) List<Choice> choices
) {

    public record Choice(
        int orderNumber,
        String label
    ) {

    }
}
