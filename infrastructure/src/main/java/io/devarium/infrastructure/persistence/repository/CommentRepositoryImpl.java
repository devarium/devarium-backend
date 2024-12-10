package io.devarium.infrastructure.persistence.repository;

import io.devarium.core.domain.comment.Comment;
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

    private CommentEntity convertToEntity(Comment comment) {
        return CommentEntity.builder()
            .content(comment.getContent())
            .build();
    }
}
