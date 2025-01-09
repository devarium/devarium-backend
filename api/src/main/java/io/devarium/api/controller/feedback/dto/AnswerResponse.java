package io.devarium.api.controller.feedback.dto;

import io.devarium.core.domain.feedback.answer.FeedbackAnswer;
import io.devarium.core.domain.feedback.answer.Rating;
import java.time.Instant;

public record AnswerResponse(
    Long id,
    String content,
    Rating rating,
    Long questionId,
    Long userId,
    Instant answeredAt
) {

    public static AnswerResponse from(FeedbackAnswer answer) {
        return new AnswerResponse(
            answer.getId(),
            answer.getContent(),
            answer.getRating(),
            answer.getQuestionId(),
            answer.getUserId(),
            answer.getAnsweredAt()
        );
    }
}

