package io.devarium.core.domain.feedback;

import io.devarium.core.domain.feedback.answer.Answer;
import io.devarium.core.domain.feedback.question.Question;
import java.util.Comparator;
import java.util.List;

public record Feedback(Question question, List<Answer> answers) {

    public static Feedback of(Question question, List<Answer> answers) {
        List<Answer> sortedAnswers = answers.stream()
            .sorted(Comparator.comparing(Answer::getAnsweredAt))
            .toList();

        return new Feedback(question, sortedAnswers);
    }
}
