package io.devarium.core.storage.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum StorageErrorCode {
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload file"),
    FILE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete file"),
    INVALID_FILE_SIZE(HttpStatus.BAD_REQUEST, "File size exceeds maximum limit"),
    INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "Unsupported file type"),
    INVALID_FILE_URL(HttpStatus.BAD_REQUEST, "Invalid file URL");

    private final HttpStatus status;
    private final String message;
}
