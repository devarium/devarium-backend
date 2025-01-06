package io.devarium.core.auth.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode {
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
    FORBIDDEN_ACCESS(
        HttpStatus.FORBIDDEN,
        "Not allowed to access the resource: %d"
    ),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid token"),
    UNAUTHENTICATED_USER(HttpStatus.UNAUTHORIZED, "Authentication required");

    private final HttpStatus status;
    private final String message;

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
