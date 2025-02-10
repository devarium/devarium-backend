package io.devarium.core.storage.port.in;

import io.devarium.core.storage.Image;
import io.devarium.core.storage.ImageType;

public interface StorageService {

    String upload(Image image, ImageType imageType);

    void delete(String fileUrl);
}
