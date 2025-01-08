package io.devarium.core.domain.feedback.answer;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FeedbackAnswer {

    private final Long id;
    private final Rating rating;
    private final Long userId;

    private String content;

    @Builder
    public FeedbackAnswer(Long id, String content, Rating rating, Long userId) {
//        validateAnswer(content, rating);
        this.id = id;
        this.content = content;
        this.rating = rating;
        this.userId = userId;
    }

//    private void validateAnswer(String content, Rating rating) {
//        if (content == null && rating == null) {
//            throw new FeedbackException(FeedbackErrorCode.ANSWER_REQUIRED);
//        }
//    }
}
