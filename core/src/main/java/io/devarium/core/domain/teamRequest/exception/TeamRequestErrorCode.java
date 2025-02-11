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
    ),
    MEMBERSHIP_ALREADY_EXISTS(
        HttpStatus.FORBIDDEN,
        "User (ID: %d) already has a membership (ID: %d) in the team (ID: %d)"
    ),
    MEMBERSHIPS_ALREADY_EXIST(
        HttpStatus.FORBIDDEN,
        "Users (ID: %s) already have memberships in the team (ID: %d)"
    ),
    TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "Team (ID: %d) not found"),
    USERS_NOT_FOUND(HttpStatus.NOT_FOUND, "Users (ID: %s) not found"),
    TEAM_REQUEST_ALREADY_ACCEPTED(
        HttpStatus.BAD_REQUEST,
        "Team request (ID: %d) is already in 'ACCEPTED' status, so it cannot be updated to '%s' status"
    );

    private final HttpStatus status;
    private final String message;

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
