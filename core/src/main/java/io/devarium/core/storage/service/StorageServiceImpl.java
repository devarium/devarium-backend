package io.devarium.core.storage.service;

import io.devarium.core.storage.Image;
import io.devarium.core.storage.ImageType;
import io.devarium.core.storage.exception.StorageErrorCode;
import io.devarium.core.storage.exception.StorageException;
import io.devarium.core.storage.port.ImageStorage;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StorageServiceImpl implements StorageService {

    private static final String KEY_FORMAT = "%s/%s-%s%s";
    private final ImageStorage imageStorage;

    @Override
    public String upload(Image image, ImageType imageType) {
        image.validateImageType(imageType);
        String key = createKey(image, imageType);
        return imageStorage.upload(image, key);
    }

    @Override
    public void delete(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank()) {
            throw new StorageException(StorageErrorCode.INVALID_FILE_URL, "File URL is empty");
        }

        String key = extractKeyFromUrl(fileUrl);
        imageStorage.delete(key);
    }

    private String createKey(Image image, ImageType imageType) {
        return KEY_FORMAT.formatted(
            imageType.getPath(),
            UUID.randomUUID(),
            Instant.now().toEpochMilli(),
            image.getExtension()
        );
    }

    private String extractKeyFromUrl(String fileUrl) {
        try {
            URI uri = new URI(fileUrl);
            URL url = uri.toURL();
            String path = url.getPath();
            return path.startsWith("/") ? path.substring(1) : path;
        } catch (URISyntaxException | MalformedURLException e) {
            throw new StorageException(StorageErrorCode.INVALID_FILE_URL, fileUrl);
        }
    }
}
