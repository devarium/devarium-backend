package io.devarium.infrastructure.persistence.repository;

import io.devarium.core.domain.comment.Comment;
import io.devarium.core.domain.comment.exception.CommentErrorCode;
import io.devarium.core.domain.comment.exception.CommentException;
import io.devarium.core.domain.comment.repository.CommentRepository;
import io.devarium.infrastructure.persistence.entity.CommentEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CommentRepositoryImpl implements CommentRepository {

    private final CommentJpaRepository commentJpaRepository;

    @Override
    public Comment save(Comment comment) {
        CommentEntity entity = convertToEntity(comment);
        CommentEntity savedEntity = commentJpaRepository.save(entity);
        return convertToDomain(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        commentJpaRepository.deleteById(id);
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return commentJpaRepository.findById(id).map(this::convertToDomain);
    }

    private Comment convertToDomain(CommentEntity entity) {
        return Comment.builder()
            .id(entity.getId())
            .content(entity.getContent())
            .createdAt(entity.getCreatedAt())
            .build();
    }

    private CommentEntity convertToEntity(Comment domain) {
        if (domain.getId() != null) {
            CommentEntity entity = commentJpaRepository.findById(domain.getId())
                .orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND, domain.getId()));
            entity.update(domain);
            return entity;
        }

        return CommentEntity.builder()
            .content(domain.getContent())
            .build();
    }
}
