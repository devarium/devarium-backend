package io.devarium.core.domain.feedback.answer;

import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Answer {

    private final Long id;
    private final Long questionId;
    private final Long userId;
    private final Instant answeredAt;
    
    private String content;

    @Builder
    public Answer(
        Long id,
        String content,
        Long questionId,
        Long userId,
        Instant answeredAt
    ) {
//        validateAnswer(content, rating);
        this.id = id;
        this.content = content;
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
