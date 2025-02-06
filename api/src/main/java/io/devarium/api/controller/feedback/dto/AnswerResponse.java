package io.devarium.api.controller.feedback.dto;

import io.devarium.core.domain.feedback.answer.Answer;
import java.time.Instant;

public record AnswerResponse(
    Long id,
    String content,
    Long questionId,
    Long userId,
    Instant answeredAt
) {

    public static AnswerResponse from(Answer answer) {
        return new AnswerResponse(
            answer.getId(),
            answer.getContent(),
            answer.getQuestionId(),
            answer.getUserId(),
            answer.getAnsweredAt()
        );
    }
}

