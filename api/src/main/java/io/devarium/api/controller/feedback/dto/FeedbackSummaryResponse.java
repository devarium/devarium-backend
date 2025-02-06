package io.devarium.api.controller.feedback.dto;

import io.devarium.core.domain.feedback.FeedbackSummary;

public record FeedbackSummaryResponse(
    QuestionResponse question,
    AnswerResponse answer
) {

    public static FeedbackSummaryResponse from(FeedbackSummary feedbackSummary) {
        return new FeedbackSummaryResponse(
            QuestionResponse.from(feedbackSummary.question()),
            AnswerResponse.from(feedbackSummary.answer())
        );
    }
}
