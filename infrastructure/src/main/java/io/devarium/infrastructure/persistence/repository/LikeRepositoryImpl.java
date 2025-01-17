package io.devarium.infrastructure.persistence.repository;

import io.devarium.core.domain.like.LikeTargetType;
import io.devarium.core.domain.like.Like;
import io.devarium.core.domain.like.repository.LikeRepository;
import io.devarium.core.domain.user.User;
import io.devarium.infrastructure.persistence.entity.LikeEntity;
import io.devarium.infrastructure.persistence.entity.UserEntity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class LikeRepositoryImpl implements LikeRepository {

    private final EntityManager entityManager;
    private final LikeJpaRepository likeJpaRepository;

    @Override
    public void save(Like like) {
        UserEntity userEntity = entityManager.getReference(UserEntity.class, like.getUserId());
        LikeEntity entity = LikeEntity.builder()
            .targetType(like.getTargetType())
            .targetId(like.getTargetId())
            .user(userEntity)
            .build();

        likeJpaRepository.save(entity);
    }

    @Override
    public boolean existsByTargetTypeAndTargetIdAndUser(LikeTargetType targetType, Long targetId, User user) {
        UserEntity userEntity = entityManager.getReference(UserEntity.class, user.getId());
        return likeJpaRepository.existsByTargetTypeAndTargetIdAndUser(targetType, targetId, userEntity);
    }

    @Override
    public void deleteByTargetTypeAndTargetIdAndUser(LikeTargetType targetType, Long targetId, User user) {
        UserEntity userEntity = entityManager.getReference(UserEntity.class, user.getId());
        likeJpaRepository.deleteByTargetTypeAndTargetIdAndUser(targetType, targetId, userEntity);
    }

    @Override
    public Long countByTargetTypeAndTargetId(LikeTargetType targetType, Long targetId) {
        return likeJpaRepository.countByTargetTypeAndTargetId(targetType, targetId);
    }
}
