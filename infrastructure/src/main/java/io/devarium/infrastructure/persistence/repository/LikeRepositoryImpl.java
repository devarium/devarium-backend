package io.devarium.infrastructure.persistence.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.devarium.core.domain.like.LikeTargetType;
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
            .targetType(like.getTargetType())
            .targetId(like.getTargetId())
            .user(userEntity)
            .build();

        likeJpaRepository.save(entity);
    }

    @Override
    public boolean existsByTargetTypeAndTargetIdAndUser(LikeTargetType targetType, Long targetId, User user) {
        QLikeEntity like = QLikeEntity.likeEntity;
        return queryFactory
            .selectOne()
            .from(like)
            .where(like.targetType.eq(targetType), like.targetId.eq(targetId), like.user.id.eq(user.getId()))
            .fetchFirst() != null;
    }

    @Override
    public void deleteByTargetTypeAndTargetIdAndUser(LikeTargetType targetType, Long targetId, User user) {
        QLikeEntity like = QLikeEntity.likeEntity;
        queryFactory.delete(like)
            .where(like.targetType.eq(targetType), like.targetId.eq(targetId), like.user.id.eq(user.getId()))
            .execute();
    }

    @Override
    public Long countByTargetTypeAndTargetId(LikeTargetType targetType, Long targetId) {
        QLikeEntity like = QLikeEntity.likeEntity;
        return queryFactory.select(like.count())
            .from(like)
            .where(like.targetType.eq(targetType), like.targetId.eq(targetId))
            .fetchOne();
    }
}
