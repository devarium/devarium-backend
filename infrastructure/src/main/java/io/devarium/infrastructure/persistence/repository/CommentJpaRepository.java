package io.devarium.infrastructure.persistence.repository;

import io.devarium.infrastructure.persistence.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentJpaRepository extends JpaRepository<CommentEntity, Long> {

}
