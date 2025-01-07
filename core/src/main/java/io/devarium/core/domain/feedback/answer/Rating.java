package io.devarium.core.domain.feedback.answer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Rating {
    STRONGLY_DISAGREE(1, "매우 그렇지 않다"),
    DISAGREE(2, "그렇지 않다"),
    NEUTRAL(3, "보통이다"),
    AGREE(4, "그렇다"),
    STRONGLY_AGREE(5, "매우 그렇다");

    private final int score;
    private final String description;
}
