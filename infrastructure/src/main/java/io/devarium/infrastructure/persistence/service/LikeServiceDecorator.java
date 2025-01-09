package io.devarium.infrastructure.persistence.service;

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
    public void like(String type, Long typeId, User user) {
        likeService.like(type, typeId, user);
    }

    @Override
    @Transactional
    public void unlike(String type, Long typeId, User user) {
        likeService.unlike(type, typeId, user);
    }
}
