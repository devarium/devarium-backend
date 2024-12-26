package io.devarium.core.auth.exception;

import lombok.Getter;

@Getter
public class TokenStorageException extends RuntimeException {

    private final TokenStorageErrorCode errorCode;

    public TokenStorageException(TokenStorageErrorCode errorCode, Object... args) {
        super(errorCode.getMessage(args));
        this.errorCode = errorCode;
    }

}
