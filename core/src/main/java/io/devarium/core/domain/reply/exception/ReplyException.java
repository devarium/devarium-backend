package io.devarium.core.domain.reply.exception;

import lombok.Getter;

@Getter
public class ReplyException extends RuntimeException {

    private final ReplyErrorCode errorCode;

    public ReplyException(final ReplyErrorCode errorCode, final Object... args) {
        super(errorCode.getMessage(args));
        this.errorCode = errorCode;
    }
}
