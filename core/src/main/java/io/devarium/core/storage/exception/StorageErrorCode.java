package io.devarium.core.storage.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum StorageErrorCode {
    EMPTY_FILE(HttpStatus.BAD_REQUEST, "File is empty"),
    INVALID_FILENAME(HttpStatus.BAD_REQUEST, "Invalid filename"),
    INVALID_FILE_SIZE(HttpStatus.BAD_REQUEST, "File size exceeds maximum limit"),
    INVALID_CONTENT_TYPE(HttpStatus.BAD_REQUEST, "Unsupported image type"),
    INVALID_PROFILE_IMAGE_TYPE(HttpStatus.BAD_REQUEST, "Profile image must be JPEG or PNG"),
    INVALID_FILE_URL(HttpStatus.BAD_REQUEST, "Invalid file URL: %s"),
    FILE_READ_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to read file"),
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload file"),
    FILE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete file");

    private final HttpStatus status;
    private final String message;
}