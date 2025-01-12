package io.devarium.core.domain.feedback;

import io.devarium.core.domain.feedback.answer.Answer;
import io.devarium.core.domain.feedback.question.Question;
import java.util.Map;

public record FeedbackSummary(Long projectId, Map<Question, Answer> questionToAnswer) {

}
