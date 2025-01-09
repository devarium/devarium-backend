package io.devarium.infrastructure.persistence.repository;

import io.devarium.infrastructure.persistence.entity.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeJpaRepository extends JpaRepository<LikeEntity, Long> {

}
