package io.devarium.core.domain.feedback;

import java.util.Comparator;
import java.util.List;

public record Feedback(Long projectId, List<QuestionWithAnswers> questionAnswers) {

    public static Feedback of(Long projectId, List<QuestionWithAnswers> questionAnswers) {
        List<QuestionWithAnswers> sortedQuestionAnswers = questionAnswers.stream()
            .sorted(Comparator.comparing(qa -> qa.question().getOrderNumber()))
            .toList();
        return new Feedback(projectId, sortedQuestionAnswers);
    }
}
