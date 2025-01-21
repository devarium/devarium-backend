package io.devarium.core.storage.port;

import io.devarium.core.storage.Image;

public interface ImageStorage {

    String upload(Image image, String key);

    void delete(String key);
}
