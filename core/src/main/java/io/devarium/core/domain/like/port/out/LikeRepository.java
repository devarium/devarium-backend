package io.devarium.core.domain.like.port.out;

import io.devarium.core.domain.like.Like;
import io.devarium.core.domain.like.LikeTargetType;
import io.devarium.core.domain.user.User;

public interface LikeRepository {

    void save(Like like);

    boolean existsByTargetTypeAndTargetIdAndUser(LikeTargetType targetType, Long targetId,
        User user);

    void deleteByTargetTypeAndTargetIdAndUser(LikeTargetType targetType, Long targetId, User user);

    Long countByTargetTypeAndTargetId(LikeTargetType targetType, Long targetId);
}
