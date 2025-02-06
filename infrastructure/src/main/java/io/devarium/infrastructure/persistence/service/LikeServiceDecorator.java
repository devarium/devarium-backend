package io.devarium.infrastructure.persistence.service;

import io.devarium.core.domain.like.Likeable;
import io.devarium.core.domain.like.service.LikeService;
import io.devarium.core.domain.like.service.LikeServiceImpl;
import io.devarium.core.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class LikeServiceDecorator implements LikeService {

    private final LikeServiceImpl likeService;

    @Override
    @Transactional
    public void like(Likeable likeable, User user) {
        likeService.like(likeable, user);
    }

    @Override
    @Transactional
    public void unlike(Likeable likeable, User user) {
        likeService.unlike(likeable, user);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getLikeCount(Likeable likeable) {
        return likeService.getLikeCount(likeable);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasUserLiked(Likeable likeable, User user) {
        return likeService.hasUserLiked(likeable, user);
    }
}
