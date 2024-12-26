package io.devarium.core.domain.comment.service;

import io.devarium.core.domain.comment.Comment;
import io.devarium.core.domain.comment.port.UpsertComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    Comment createComment(UpsertComment request);

    Comment getComment(Long commentId);

    Page<Comment> getCommentsByPostId(Long postId, Pageable pageable);

    Comment updateComment(Long commentId, UpsertComment request);

    void deleteComment(Long commentId);

    void deleteCommentsByPostId(Long postId);
}
