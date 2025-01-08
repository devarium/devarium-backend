package io.devarium.core.domain.feedback.exception;

import lombok.Getter;

@Getter
public class FeedbackException extends RuntimeException {

    private final FeedbackErrorCode errorCode;

    public FeedbackException(FeedbackErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
