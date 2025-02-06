package io.devarium.core.domain.teamRequest.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TeamRequestErrorCode {
    TEAM_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "Team request (ID: %d) not found"),
    TEAM_REQUESTS_NOT_FOUND(HttpStatus.NOT_FOUND, "Team requests (ID: %s) not found"),
    TEAM_REQUEST_NOT_FOUND_BY_USER(
        HttpStatus.NOT_FOUND,
        "Team request (ID: %d) not found by User (ID: %d)"
    );

    private final HttpStatus status;
    private final String message;

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
