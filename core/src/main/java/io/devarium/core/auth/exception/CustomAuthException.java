package io.devarium.core.auth.exception;

import lombok.Getter;

@Getter
public class CustomAuthException extends RuntimeException {

    private final AuthErrorCode errorCode;

    public CustomAuthException(AuthErrorCode errorCode, Object... args) {
        super(errorCode.getMessage(args));
        this.errorCode = errorCode;
    }
}
