package io.devarium.core.domain.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found with id: %d"),
    USER_EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found with email: %s"),
    USER_WITHDRAW_FAILED(
        HttpStatus.FORBIDDEN,
        "User (ID: %d) cannot withdraw. Must not be a team leader"
    ),
    USER_IS_DELETED(HttpStatus.NOT_FOUND, "User (ID: %d) has been deleted");

    private final HttpStatus status;
    private final String message;

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
