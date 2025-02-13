package io.devarium.infrastructure.persistence.repository;

import io.devarium.core.domain.comment.Comment;
import io.devarium.core.domain.comment.exception.CommentErrorCode;
import io.devarium.core.domain.comment.exception.CommentException;
import io.devarium.core.domain.comment.port.out.CommentRepository;
import io.devarium.infrastructure.persistence.entity.CommentEntity;
import io.devarium.infrastructure.persistence.entity.PostEntity;
import io.devarium.infrastructure.persistence.entity.UserEntity;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CommentRepositoryImpl implements CommentRepository {

    private final EntityManager entityManager;
    private final CommentJpaRepository commentJpaRepository;

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() != null) {
            CommentEntity entity = commentJpaRepository.findById(comment.getId())
                .orElseThrow(() -> new CommentException(
                    CommentErrorCode.COMMENT_NOT_FOUND,
                    comment.getId())
                );
            entity.update(comment);
            return commentJpaRepository.save(entity).toDomain();
        }

        PostEntity post = entityManager.getReference(PostEntity.class, comment.getPostId());
        UserEntity user = entityManager.getReference(UserEntity.class, comment.getUserId());

        CommentEntity entity = CommentEntity.fromDomain(comment, post, user);
        return commentJpaRepository.save(entity).toDomain();
    }

    @Override
    public void deleteById(Long id) {
        commentJpaRepository.deleteById(id);
    }

    @Override
    public void deleteByPostId(Long postId) {
        commentJpaRepository.deleteByPostId(postId);
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return commentJpaRepository.findById(id).map(CommentEntity::toDomain);
    }

    @Override
    public Page<Comment> findByPostId(Long postId, Pageable pageable) {
        return commentJpaRepository.findByPostId(postId, pageable).map(CommentEntity::toDomain);
    }
}
