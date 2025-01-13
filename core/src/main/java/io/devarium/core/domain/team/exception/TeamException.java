package io.devarium.core.domain.team.exception;

import lombok.Getter;

@Getter
public class TeamException extends RuntimeException {

    private final TeamErrorCode errorCode;

    public TeamException(TeamErrorCode errorCode, Object... args) {
        super(errorCode.getMessage(args));
        this.errorCode = errorCode;
    }
}
