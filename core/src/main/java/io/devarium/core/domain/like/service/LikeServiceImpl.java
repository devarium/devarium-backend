package io.devarium.core.domain.like.service;

import io.devarium.core.domain.like.Like;
import io.devarium.core.domain.like.Likeable;
import io.devarium.core.domain.like.repository.LikeRepository;
import io.devarium.core.domain.user.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;

    @Override
    public void like(Likeable likeable, User user) {
        if (likeRepository.existsByEntityTypeAndEntityIdAndUser(likeable.getType(), likeable.getId(), user)) {
            unlike(likeable, user);
        } else {
            Like like = Like.builder()
                .entityType(likeable.getType())
                .entityId(likeable.getId())
                .userId(user.getId())
                .build();
            likeRepository.save(like);
        }
    }

    @Override
    public void unlike(Likeable likeable, User user) {
        likeRepository.deleteByEntityTypeAndEntityIdAndUser(likeable.getType(), likeable.getId(), user);
    }

    @Override
    public Long getLikeCount(Likeable likeable) {
        return likeRepository.countByEntityTypeAndEntityId(likeable.getType(), likeable.getId());
    }
}
