package io.devarium.api.controller.feedback.dto;

import io.devarium.core.domain.feedback.Feedback;
import java.util.List;

public record FeedbackResponse(
    QuestionResponse question,
    List<AnswerResponse> answers
) {

    public static FeedbackResponse from(Feedback feedback) {
        return new FeedbackResponse(
            QuestionResponse.from(feedback.question()),
            feedback.answers().stream()
                .map(AnswerResponse::from)
                .toList()
        );
    }
}
