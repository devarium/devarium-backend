package io.devarium.core.domain.feedback.answer;

import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Answer {

    private final Long id;
    private final Rating rating;
    private final Long questionId;
    private final Long userId;

    private String content;
    private Instant answeredAt;

    @Builder
    public Answer(
        Long id,
        String content,
        Rating rating,
        Long questionId,
        Long userId,
        Instant answeredAt
    ) {
//        validateAnswer(content, rating);
        this.id = id;
        this.content = content;
        this.rating = rating;
        this.questionId = questionId;
        this.userId = userId;
        this.answeredAt = answeredAt;
    }

//    private void validateAnswer(String content, Rating rating) {
//        if (content == null && rating == null) {
//            throw new FeedbackException(FeedbackErrorCode.ANSWER_REQUIRED);
//        }
//    }
}
