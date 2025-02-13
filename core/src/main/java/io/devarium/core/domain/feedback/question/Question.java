package io.devarium.core.domain.feedback.question;

import io.devarium.core.domain.feedback.exception.FeedbackErrorCode;
import io.devarium.core.domain.feedback.exception.FeedbackException;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Question {

    private final Long id;
    private final Long projectId;

    private int orderNumber;
    private QuestionContent questionContent;
    private QuestionType type;
    private boolean required;

    @Builder
    public Question(
        Long id,
        int orderNumber,
        QuestionContent questionContent,
        QuestionType type,
        boolean required,
        Long projectId
    ) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.questionContent = questionContent;
        this.type = type;
        this.required = required;
        this.projectId = projectId;
    }

    public void updateOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void update(QuestionContent content, QuestionType type, Boolean required) {
        this.questionContent = content;
        this.type = type;
        this.required = required;
    }

    public void validateProjectAccess(Long projectId) {
        if (!Objects.equals(this.projectId, projectId)) {
            throw new FeedbackException(FeedbackErrorCode.INVALID_PROJECT_ACCESS);
        }
    }
}
