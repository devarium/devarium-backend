package io.devarium.infrastructure.persistence.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.devarium.core.domain.like.EntityType;
import io.devarium.core.domain.like.Like;
import io.devarium.core.domain.like.repository.LikeRepository;
import io.devarium.core.domain.user.User;
import io.devarium.core.domain.user.exception.UserErrorCode;
import io.devarium.core.domain.user.exception.UserException;
import io.devarium.infrastructure.persistence.entity.LikeEntity;
import io.devarium.infrastructure.persistence.entity.QLikeEntity;
import io.devarium.infrastructure.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class LikeRepositoryImpl implements LikeRepository {

    private final LikeJpaRepository likeJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public void save(Like like) {
        UserEntity userEntity = userJpaRepository.findById(like.getUserId())
            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND, like.getUserId()));

        LikeEntity entity = LikeEntity.builder()
            .entityType(like.getEntityType())
            .entityId(like.getEntityId())
            .user(userEntity)
            .build();

        likeJpaRepository.save(entity);
    }

    @Override
    public boolean existsByEntityTypeAndEntityIdAndUser(EntityType entityType, Long typeId, User user) {
        QLikeEntity like = QLikeEntity.likeEntity;
        return queryFactory
            .selectOne()
            .from(like)
            .where(like.entityType.eq(entityType), like.entityId.eq(typeId), like.user.id.eq(user.getId()))
            .fetchFirst() != null;
    }

    @Override
    public void deleteByEntityTypeAndEntityIdAndUser(EntityType entityType, Long typeId, User user) {
        QLikeEntity like = QLikeEntity.likeEntity;
        queryFactory.delete(like)
            .where(like.entityType.eq(entityType), like.entityId.eq(typeId), like.user.id.eq(user.getId()))
            .execute();
    }

    @Override
    public Long countByEntityTypeAndEntityId(EntityType entityType, Long typeId) {
        QLikeEntity like = QLikeEntity.likeEntity;
        return queryFactory.select(like.count())
            .from(like)
            .where(like.entityType.eq(entityType), like.entityId.eq(typeId))
            .fetchOne();
    }
}
