package io.devarium.infrastructure.persistence.repository;

import io.devarium.infrastructure.persistence.entity.ReplyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyJpaRepository extends JpaRepository<ReplyEntity, Long> {

    Page<ReplyEntity> findByCommentId(Long commentId, Pageable pageable);

    void deleteByCommentId(Long commentId);

    void deleteByCommentPostId(Long postId);
}
