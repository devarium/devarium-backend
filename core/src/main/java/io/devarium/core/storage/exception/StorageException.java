package io.devarium.core.storage.exception;

import lombok.Getter;

@Getter
public class StorageException extends RuntimeException {

    private final StorageErrorCode errorCode;

    public StorageException(StorageErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public StorageException(StorageErrorCode errorCode, String detail) {
        super(errorCode.getMessage() + ": " + detail);
        this.errorCode = errorCode;
    }

    public StorageException(StorageErrorCode errorCode, Object... args) {
        super(errorCode.getMessage(args));
        this.errorCode = errorCode;
    }

    public StorageException(StorageErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }
}
