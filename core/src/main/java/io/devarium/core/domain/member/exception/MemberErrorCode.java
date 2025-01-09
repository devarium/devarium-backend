package io.devarium.core.domain.member.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode {
    MEMBER_NOT_FOUND(
        HttpStatus.NOT_FOUND,
        "Member (ID: %d) not found"
    ),
    MEMBER_NOT_IN_TEAM(
        HttpStatus.FORBIDDEN,
        "User (ID: %d) is not part of team (ID: %d)"
    ),
    FORBIDDEN_ACCESS(
        HttpStatus.FORBIDDEN,
        "Member (ID: %d) - User (ID: %d) does not have permission to modify team (ID: %d)"
    ),
    UPDATE_LEADER_ONLY(
        HttpStatus.FORBIDDEN,
        "The SUPER_ADMIN role of member (ID: %d) can only be changed using updateLeader"
    ),
    MEMBER_ROLE_HIERARCHY_VIOLATION(
        HttpStatus.FORBIDDEN,
        "You do not have permission to modify the roles of members with a higher permission level"
    ),
    FIRST_MEMBER_ONLY(
        HttpStatus.FORBIDDEN,
        "User (ID: %d) can only be used when creating the first member of the team (ID: %d) as a leader"
    );

    private final HttpStatus status;
    private final String message;

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
