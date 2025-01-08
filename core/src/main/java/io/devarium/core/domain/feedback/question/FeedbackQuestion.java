package io.devarium.core.domain.feedback.question;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FeedbackQuestion {

    private final Long id;
    private final int orderNumber;
    private final QuestionType type;

    private String content;
    private boolean required;

    @Builder
    public FeedbackQuestion(
        Long id,
        int orderNumber,
        String content,
        QuestionType type,
        boolean required
    ) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.content = content;
        this.type = type;
        this.required = required;
    }
}
