package io.devarium.core.domain.like.service;

import io.devarium.core.domain.user.User;

public interface LikeService {

    void like(String type, Long typeId, User user);

    void unlike(String type, Long typeId, User user);
}
