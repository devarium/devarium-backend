package io.devarium.core.domain.comment.service;

import io.devarium.core.domain.comment.Comment;
import io.devarium.core.domain.comment.command.UpsertCommentCommand;

public interface CommentService {

    Comment createComment(UpsertCommentCommand command);
}
