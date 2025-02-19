package io.devarium.core.domain.like.service;

import io.devarium.core.domain.like.Like;
import io.devarium.core.domain.like.Likeable;
import io.devarium.core.domain.like.port.in.LikeService;
import io.devarium.core.domain.like.port.out.LikeRepository;
import io.devarium.core.domain.user.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;

    @Override
    public void like(Likeable likeable, User user) {
        if (hasUserLiked(likeable, user)) {
            unlike(likeable, user);
        } else {
            Like like = Like.builder()
                .targetType(likeable.getType())
                .targetId(likeable.getId())
                .userId(user.getId())
                .build();
            likeRepository.save(like);
        }
    }

    @Override
    public void unlike(Likeable likeable, User user) {
        likeRepository.deleteByTargetTypeAndTargetIdAndUser(likeable.getType(), likeable.getId(),
            user);
    }

    @Override
    public Long getLikeCount(Likeable likeable) {
        return likeRepository.countByTargetTypeAndTargetId(likeable.getType(), likeable.getId());
    }

    @Override
    public boolean hasUserLiked(Likeable likeable, User user) {
        return likeRepository.existsByTargetTypeAndTargetIdAndUser(likeable.getType(),
            likeable.getId(), user);
    }
}
