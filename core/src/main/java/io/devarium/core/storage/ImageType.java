package io.devarium.core.storage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImageType {
    PROFILE("images/profiles"),
    POST("images/posts"),
    PROJECT("images/projects");

    private final String path;
}
