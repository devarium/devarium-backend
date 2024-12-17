package io.devarium.core.domain.comment.exception;

import lombok.Getter;

@Getter
public class CommentException extends RuntimeException {

    private final CommentErrorCode errorCode;

    public CommentException(CommentErrorCode errorCode, Object... args) {
        super(errorCode.getMessage(args));
        this.errorCode = errorCode;
    }
}
