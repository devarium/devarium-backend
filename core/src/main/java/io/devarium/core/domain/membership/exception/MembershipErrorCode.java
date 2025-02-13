package io.devarium.core.domain.membership.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MembershipErrorCode {
    MEMBERSHIPS_NOT_FOUND(
        HttpStatus.NOT_FOUND,
        "Memberships (ID: %s) not found"
    ),
    MEMBERSHIP_NOT_IN_TEAM(
        HttpStatus.FORBIDDEN,
        "User (ID: %d) is not part of team (ID: %d)"
    ),
    FORBIDDEN_ACCESS(
        HttpStatus.FORBIDDEN,
        "Membership (ID: %d) - User (ID: %d) does not have permission to modify team (ID: %d)"
    ),
    INVALID_MEMBER_ROLE(
        HttpStatus.FORBIDDEN,
        "Cannot change to 'LEADER' role"
    ),
    FIRST_MEMBERSHIP_ONLY(
        HttpStatus.FORBIDDEN,
        "User (ID: %d) can only be used when creating the first member of the team (ID: %d) as a leader"
    ),
    INVALID_UPDATE_ENTITY(
        HttpStatus.NOT_FOUND,
        "All requested entities must be updatable entities"
    ),
    TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "Team (ID: %d) not found"),
    LEADER_ROLE_UNMODIFIABLE(HttpStatus.FORBIDDEN,
        "'LEADER' Role of member (ID: %d) cannot be modified");

    private final HttpStatus status;
    private final String message;

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
