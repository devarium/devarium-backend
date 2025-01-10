package io.devarium.core.domain.like.service;

import io.devarium.core.domain.comment.repository.CommentRepository;
import io.devarium.core.domain.like.EntityType;
import io.devarium.core.domain.like.Like;
import io.devarium.core.domain.like.exception.LikeErrorCode;
import io.devarium.core.domain.like.exception.LikeException;
import io.devarium.core.domain.like.repository.LikeRepository;
import io.devarium.core.domain.post.repository.PostRepository;
import io.devarium.core.domain.reply.repository.ReplyRepository;
import io.devarium.core.domain.user.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;

    @Override
    public void like(String type, Long typeId, User user) {
        EntityType entityType = EntityType.fromString(type);

        boolean exists = switch (entityType) {
            case POST -> postRepository.existsById(typeId);
            case COMMENT -> commentRepository.existsById(typeId);
            case REPLY -> replyRepository.existsById(typeId);
        };

        if (!exists) {
            throw new LikeException(LikeErrorCode.ENTITY_ID_NOT_FOUND, typeId);
        }

        if (likeRepository.existsByEntityTypeAndEntityIdAndUser(entityType, typeId, user)) {
            unlike(type, typeId, user);
        } else {
            Like like = Like.builder()
                .entityType(entityType)
                .entityId(typeId)
                .userId(user.getId())
                .build();
            likeRepository.save(like);
        }
    }

    @Override
    public void unlike(String type, Long typeId, User user) {
        EntityType entityType = EntityType.fromString(type);
        likeRepository.deleteByEntityTypeAndEntityIdAndUser(entityType, typeId, user);
    }

    @Override
    public Long countLikes(String type, Long typeId) {
        EntityType entityType = EntityType.fromString(type);
        return likeRepository.countByEntityTypeAndEntityId(entityType, typeId);
    }
}
