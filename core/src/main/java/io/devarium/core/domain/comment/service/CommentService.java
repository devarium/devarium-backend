package io.devarium.core.domain.comment.service;

import io.devarium.core.domain.comment.Comment;
import io.devarium.core.domain.comment.command.UpsertCommentCommand;

public interface CommentService {

    Comment createComment(UpsertCommentCommand command);

    Comment getComment(Long commentId);

    Comment updateComment(Long commentId, UpsertCommentCommand command);

    void deleteComment(Long commentId);
}
