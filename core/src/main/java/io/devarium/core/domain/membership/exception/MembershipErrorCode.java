package io.devarium.core.domain.membership.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MembershipErrorCode {
    MEMBERSHIP_NOT_FOUND(
        HttpStatus.NOT_FOUND,
        "Membership (ID: %d) not found"
    ),
    MEMBERSHIP_NOT_IN_TEAM(
        HttpStatus.FORBIDDEN,
        "User (ID: %d) is not part of team (ID: %d)"
    ),
    FORBIDDEN_ACCESS(
        HttpStatus.FORBIDDEN,
        "Membership (ID: %d) - User (ID: %d) does not have permission to modify team (ID: %d)"
    ),
    UPDATE_LEADER_ONLY(
        HttpStatus.FORBIDDEN,
        "The SUPER_ADMIN role of member (ID: %d) can only be changed using updateLeader"
    ),
    MEMBER_ROLE_HIERARCHY_VIOLATION(
        HttpStatus.FORBIDDEN,
        "You do not have permission to modify the roles of members with a higher permission level"
    ),
    FIRST_MEMBERSHIP_ONLY(
        HttpStatus.FORBIDDEN,
        "User (ID: %d) can only be used when creating the first member of the team (ID: %d) as a leader"
    ),
    USER_DELETED(
        HttpStatus.NOT_FOUND,
        "User (ID: %d) is deleted"
    ),
    TEAM_DELETED(
        HttpStatus.NOT_FOUND,
        "Team (ID: %d) is deleted"
    );

    private final HttpStatus status;
    private final String message;

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
