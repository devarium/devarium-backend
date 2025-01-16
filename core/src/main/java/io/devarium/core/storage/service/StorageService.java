package io.devarium.core.storage.service;

import io.devarium.core.storage.Image;
import io.devarium.core.storage.ImageType;

public interface StorageService {

    String upload(Image image, ImageType imageType);

    void delete(String fileUrl);
}
