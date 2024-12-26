package io.devarium.infrastructure.persistence.repository;

import io.devarium.infrastructure.persistence.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentJpaRepository extends JpaRepository<CommentEntity, Long> {

    Page<CommentEntity> findByPostId(Long postId, Pageable pageable);
}
