package io.devarium.core.domain.membership.exception;

import lombok.Getter;

@Getter
public class MembershipException extends RuntimeException {

    private final MembershipErrorCode errorCode;

    public MembershipException(MembershipErrorCode errorCode, Object... args) {
        super(errorCode.getMessage(args));
        this.errorCode = errorCode;
    }
}
