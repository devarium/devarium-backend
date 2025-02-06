package io.devarium.core.domain.feedback;

import io.devarium.core.domain.feedback.answer.Answer;
import io.devarium.core.domain.feedback.question.Question;

public record FeedbackSummary(Question question, Answer answer) {

    public static FeedbackSummary of(Question question, Answer answer) {
        return new FeedbackSummary(question, answer);
    }
}
