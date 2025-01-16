package io.devarium.core.domain.member.exception;

import lombok.Getter;

@Getter
public class MemberException extends RuntimeException {

    private final MemberErrorCode errorCode;

    public MemberException(MemberErrorCode errorCode, Object... args) {
        super(errorCode.getMessage(args));
        this.errorCode = errorCode;
    }
}
