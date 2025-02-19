package io.devarium.core.domain.feedback.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FeedbackErrorCode {
    ANSWER_NOT_FOUND(HttpStatus.NOT_FOUND, "Answer not found with id: %d"),
    QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "Question not found with id: %d"),
    INVALID_PROJECT_ACCESS(HttpStatus.FORBIDDEN, "Question is not associated with this project"),
    MEMBERSHIP_NOT_FOUND(
        HttpStatus.NOT_FOUND,
        "Membership not found by team (ID: %d) and user (ID: %d)"
    );

    private final HttpStatus status;
    private final String message;

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
