package io.devarium.api.controller.feedback.dto;

import io.devarium.core.domain.feedback.question.FeedbackQuestion;
import io.devarium.core.domain.feedback.question.QuestionType;

public record QuestionResponse(
    Long id,
    int orderNumber,
    String content,
    QuestionType type,
    boolean required,
    Long projectId
) {

    public static QuestionResponse from(FeedbackQuestion question) {
        return new QuestionResponse(
            question.getId(),
            question.getOrderNumber(),
            question.getContent(),
            question.getType(),
            question.isRequired(),
            question.getProjectId()
        );
    }
}
