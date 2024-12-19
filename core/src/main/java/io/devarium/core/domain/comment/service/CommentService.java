package io.devarium.core.domain.comment.service;

import io.devarium.core.domain.comment.Comment;
import io.devarium.core.domain.comment.port.UpsertComment;

public interface CommentService {

    Comment createComment(UpsertComment request);

    Comment getComment(Long commentId);

    Comment updateComment(Long commentId, UpsertComment request);

    void deleteComment(Long commentId);
}
