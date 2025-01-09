package io.devarium.core.domain.like.exception;

import lombok.Getter;

@Getter
public class LikeException extends RuntimeException {

    private final LikeErrorCode errorCode;

    public LikeException(LikeErrorCode errorCode, Object... args) {
        super(errorCode.getMessage(args));
        this.errorCode = errorCode;
    }

}
