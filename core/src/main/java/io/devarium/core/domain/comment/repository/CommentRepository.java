package io.devarium.core.domain.comment.repository;

import io.devarium.core.domain.comment.Comment;
import java.util.Optional;

public interface CommentRepository {

    Comment save(Comment comment);

    Optional<Comment> findById(Long id);
}
