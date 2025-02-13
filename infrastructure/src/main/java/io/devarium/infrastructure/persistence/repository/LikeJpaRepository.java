package io.devarium.infrastructure.persistence.repository;

import io.devarium.core.domain.like.LikeTargetType;
import io.devarium.infrastructure.persistence.entity.LikeEntity;
import io.devarium.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeJpaRepository extends JpaRepository<LikeEntity, Long> {

    boolean existsByTargetTypeAndTargetIdAndUser(LikeTargetType targetType, Long targetId,
        UserEntity user);

    void deleteByTargetTypeAndTargetIdAndUser(LikeTargetType targetType, Long targetId,
        UserEntity user);

    Long countByTargetTypeAndTargetId(LikeTargetType targetType, Long targetId);
}
