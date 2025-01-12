package io.devarium.core.domain.feedback.question;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Question {

    private final Long id;
    private final Long projectId;
    private final QuestionType type;

    private int orderNumber;
    private String content;
    private boolean required;

    @Builder
    public Question(
        Long id,
        int orderNumber,
        String content,
        QuestionType type,
        boolean required,
        Long projectId
    ) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.content = content;
        this.type = type;
        this.required = required;
        this.projectId = projectId;
    }

    public void update(int orderNumber, String content, Boolean required) {
        this.orderNumber = orderNumber;
        this.content = content;
        this.required = required;
    }
}
