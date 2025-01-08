package io.devarium.core.storage;

import io.devarium.core.storage.exception.StorageErrorCode;
import io.devarium.core.storage.exception.StorageException;
import java.io.IOException;
import java.util.Set;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class Image {

    private static final int MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final Set<String> ALLOWED_MIME_TYPES = Set.of(
        "image/jpeg", "image/png", "image/gif", "image/webp"
    );
    private static final Set<String> ALLOWED_PROFILE_MIME_TYPES = Set.of(
        "image/jpeg", "image/png"
    );

    private final byte[] content;
    private final String filename;
    private final String contentType;

    private Image(byte[] content, String filename, String contentType) {
        validateContent(content);
        validateFilename(filename);
        validateContentType(contentType);

        this.content = content;
        this.filename = filename;
        this.contentType = contentType;
    }

    public static Image from(MultipartFile file) {
        try {
            return new Image(
                file.getBytes(),
                file.getOriginalFilename(),
                file.getContentType()
            );
        } catch (IOException e) {
            throw new StorageException(StorageErrorCode.FILE_READ_FAILED, e);
        }
    }

    public String getExtension() {
        int lastDotIndex = filename.lastIndexOf('.');
        return lastDotIndex > -1 ? filename.substring(lastDotIndex) : "";
    }

    public void validateImageType(ImageType imageType) {
        if (!ALLOWED_MIME_TYPES.contains(contentType)) {
            throw new StorageException(StorageErrorCode.INVALID_CONTENT_TYPE);
        }
        if (imageType == ImageType.PROFILE && !ALLOWED_PROFILE_MIME_TYPES.contains(contentType)) {
            throw new StorageException(StorageErrorCode.INVALID_PROFILE_IMAGE_TYPE);
        }
    }

    private void validateContent(byte[] content) {
        if (content == null || content.length == 0) {
            throw new StorageException(StorageErrorCode.EMPTY_FILE);
        }
        if (content.length > MAX_FILE_SIZE) {
            throw new StorageException(
                StorageErrorCode.INVALID_FILE_SIZE,
                content.length / 1024.0 / 1024.0,
                MAX_FILE_SIZE / 1024.0 / 1024.0
            );
        }
    }

    private void validateFilename(String filename) {
        if (filename == null || filename.isBlank()) {
            throw new StorageException(StorageErrorCode.INVALID_FILENAME);
        }
    }

    private void validateContentType(String contentType) {
        if (contentType == null || contentType.isBlank()) {
            throw new StorageException(StorageErrorCode.INVALID_CONTENT_TYPE);
        }
    }
}
