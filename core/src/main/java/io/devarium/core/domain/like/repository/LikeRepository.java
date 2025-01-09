package io.devarium.core.domain.like.repository;

import io.devarium.core.domain.like.EntityType;
import io.devarium.core.domain.like.Like;
import io.devarium.core.domain.user.User;

public interface LikeRepository {

    void save(Like like);

    boolean existsByEntityTypeAndEntityIdAndUser(EntityType entityType, Long typeId, User user);

    void deleteByEntityTypeAndEntityIdAndUser(EntityType entityType, Long typeId, User user);
}
