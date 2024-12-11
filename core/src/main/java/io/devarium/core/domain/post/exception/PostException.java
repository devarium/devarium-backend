package io.devarium.core.domain.post.exception;

import lombok.Getter;

@Getter
public class PostException extends RuntimeException {

    private final PostErrorCode errorCode;

    public PostException(PostErrorCode errorCode, Object... args) {
        super(errorCode.getMessage(args));
        this.errorCode = errorCode;
    }
}
