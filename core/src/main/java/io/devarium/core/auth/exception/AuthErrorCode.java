package io.devarium.core.auth.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode {
    ACCESS_TOKEN_IS_NULL(HttpStatus.BAD_REQUEST, "Access Token is null"),
    GITHUB_ACCESS_TOKEN_IS_NULL(HttpStatus.BAD_REQUEST, "Github Access Token is null"),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "User not found"),

    GOOGLE_TOKEN_REQUEST_FAILED(
        HttpStatus.BAD_REQUEST,
        "Failed to request Google access token: %s"
    ),
    GOOGLE_INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid Google access token"),
    GOOGLE_USER_INFO_REQUEST_FAILED(
        HttpStatus.BAD_REQUEST,
        "Failed to request Google user info: %s"
    ),
    GOOGLE_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Google server error occurred: %s"),
    GOOGLE_INVALID_CODE(HttpStatus.BAD_REQUEST, "Invalid authorization code"),

    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "forbidden access"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "invalid token"),
    UNAUTHENTICATED_USER(HttpStatus.UNAUTHORIZED, "unauthenticated user attempt"),
    UNKNOWN_PROVIDER(HttpStatus.NOT_FOUND, "Unknown provider: %s");

    private final HttpStatus status;
    private final String message;

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
