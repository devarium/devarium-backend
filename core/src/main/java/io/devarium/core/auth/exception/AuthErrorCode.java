package io.devarium.core.auth.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode {
    ACCESS_TOKEN_IS_NULL(HttpStatus.BAD_REQUEST, "Access Token is null"),
    GOOGLE_ACCESS_TOKEN_IS_NULL(HttpStatus.BAD_REQUEST, "Google Access Token is null"),
    GITHUB_ACCESS_TOKEN_IS_NULL(HttpStatus.BAD_REQUEST, "Github Access Token is null"),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "User not found");
    
    private final HttpStatus status;
    private final String message;

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
