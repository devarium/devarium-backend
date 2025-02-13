package io.devarium.api.controller.feedback.dto;

import io.devarium.core.domain.feedback.question.Question;
import io.devarium.core.domain.feedback.question.QuestionContent;
import io.devarium.core.domain.feedback.question.QuestionType;

public record QuestionResponse(
    Long id,
    int orderNumber,
    QuestionContent content,
    QuestionType type,
    boolean required,
    Long projectId
) {

    public static QuestionResponse from(Question question) {
        return new QuestionResponse(
            question.getId(),
            question.getOrderNumber(),
            question.getQuestionContent(),
            question.getType(),
            question.isRequired(),
            question.getProjectId()
        );
    }
}
