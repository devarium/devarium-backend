package io.devarium.core.domain.feedback;

import io.devarium.core.domain.feedback.answer.Answer;
import io.devarium.core.domain.feedback.exception.FeedbackErrorCode;
import io.devarium.core.domain.feedback.exception.FeedbackException;
import io.devarium.core.domain.feedback.question.Question;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Feedback {

    private final Long projectId;
    private final Map<Question, Answer> questionAnswers =
        new TreeMap<>(Comparator.comparing(Question::getOrderNumber));

    @Builder
    public Feedback(Long projectId, List<Question> questions) {
        this.projectId = projectId;
        questions.forEach(q -> questionAnswers.put(q, null));
    }

    public void addAnswer(Question question, Answer answer) {
        if (!questionAnswers.containsKey(question)) {
            throw new FeedbackException(FeedbackErrorCode.QUESTION_NOT_FOUND);
        }
        questionAnswers.put(question, answer);
    }

    public boolean isCompleted() {
        return questionAnswers.entrySet().stream()
            .filter(entry -> entry.getKey().isRequired())
            .allMatch(entry -> entry.getValue() != null);
    }
}
