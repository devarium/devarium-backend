package io.devarium.infrastructure.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import io.devarium.core.storage.Image;
import io.devarium.core.storage.exception.StorageErrorCode;
import io.devarium.core.storage.exception.StorageException;
import io.devarium.core.storage.port.ImageStorage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j(topic = "S3ImageStorage")
public class S3ImageStorage implements ImageStorage {

    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public String upload(Image image, String key) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(image.getContentType());
        metadata.setContentLength(image.getContent().length);

        try (InputStream inputStream = new ByteArrayInputStream(image.getContent())) {
            log.debug(
                "Uploading image to S3 - bucket: {}, key: {}, contentType: {}, size: {}",
                bucket, key, image.getContentType(),
                image.getContent().length
            );

            s3Client.putObject(new PutObjectRequest(bucket, key, inputStream, metadata));
            return s3Client.getUrl(bucket, key).toString();
        } catch (IOException e) {
            log.error("Failed to read image content - key: {}", key, e);

            throw new StorageException(
                StorageErrorCode.FILE_READ_FAILED,
                "Failed to read image content: %s".formatted(e.getMessage())
            );
        } catch (AmazonS3Exception e) {
            log.error(
                "S3 upload failed - bucket: {}, key: {}, errorCode: {}, errorMessage: {}",
                bucket, key, e.getErrorCode(), e.getErrorMessage()
            );

            throw new StorageException(
                StorageErrorCode.FILE_UPLOAD_FAILED,
                "S3 upload failed: %s".formatted(e.getErrorMessage())
            );
        }
    }

    @Override
    public void delete(String key) {
        try {
            log.debug("Deleting image from S3 - bucket: {}, key: {}", bucket, key);

            s3Client.deleteObject(bucket, key);
        } catch (AmazonS3Exception e) {
            log.error(
                "S3 delete failed - bucket: {}, key: {}, errorCode: {}, errorMessage: {}",
                bucket, key, e.getErrorCode(), e.getErrorMessage()
            );

            throw new StorageException(
                StorageErrorCode.FILE_DELETE_FAILED,
                "S3 delete failed: %s".formatted(e.getErrorMessage())
            );
        }
    }
}
