package io.devarium.core.domain.teamRequest.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TeamRequestErrorCode {
    TEAM_REQUESTS_NOT_FOUND(HttpStatus.NOT_FOUND, "Team requests (ID: %s) not found");

    private final HttpStatus status;
    private final String message;

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
