package io.devarium.core.domain.like.port.in;

import io.devarium.core.domain.like.Likeable;
import io.devarium.core.domain.user.User;

public interface LikeService {

    void like(Likeable likeable, User user);

    void unlike(Likeable likeable, User user);

    Long getLikeCount(Likeable likeable);

    boolean hasUserLiked(Likeable likeable, User user);
}
