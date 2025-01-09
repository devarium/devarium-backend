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
    FIRST_MEMBER_ONLY(
        HttpStatus.FORBIDDEN,
        "This can only be used when creating the first member of the team"
    );

    private final HttpStatus status;
    private final String message;

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
