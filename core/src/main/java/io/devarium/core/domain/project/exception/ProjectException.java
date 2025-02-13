package io.devarium.core.domain.project.exception;

import lombok.Getter;

@Getter
public class ProjectException extends RuntimeException {

    private final ProjectErrorCode errorCode;

    public ProjectException(ProjectErrorCode errorCode, Object... args) {
        super(errorCode.getMessage(args));
        this.errorCode = errorCode;
    }
}
