package io.devarium.core.domain.teamRequest.exception;

import lombok.Getter;

@Getter
public class TeamRequestException extends RuntimeException {

    private final TeamRequestErrorCode errorCode;

    public TeamRequestException(TeamRequestErrorCode errorCode, Object... args) {
        super(errorCode.getMessage(args));
        this.errorCode = errorCode;
    }
}
