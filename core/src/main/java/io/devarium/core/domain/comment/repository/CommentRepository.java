package io.devarium.core.domain.comment.repository;

import io.devarium.core.domain.comment.Comment;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepository {

    Comment save(Comment comment);

    void deleteById(Long id);

    void deleteCommentsByPostId(Long postId);

    Optional<Comment> findById(Long id);

    Page<Comment> findByPostId(Long postId, Pageable pageable);
}
