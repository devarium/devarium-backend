package io.devarium.core.domain.feedback;

import io.devarium.core.domain.feedback.answer.Answer;
import io.devarium.core.domain.feedback.question.Question;
import java.util.List;

public record QuestionWithAnswers(
    Question question,
    List<Answer> answers
) {

}
