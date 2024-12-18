package io.devarium.infrastructure.persistence.service;

import io.devarium.core.domain.comment.Comment;
import io.devarium.core.domain.comment.command.UpsertCommentCommand;
import io.devarium.core.domain.comment.service.CommentService;
import io.devarium.core.domain.comment.service.CommentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class CommentServiceDecorator implements CommentService {

    private final CommentServiceImpl commentService;

    @Override
    @Transactional
    public Comment createComment(UpsertCommentCommand command) {
        return commentService.createComment(command);
    }

    @Override
    @Transactional(readOnly = true)
    public Comment getComment(Long commentId) {
        return commentService.getComment(commentId);
    }

    @Override
    @Transactional
    public Comment updateComment(Long commentId, UpsertCommentCommand command) {
        return commentService.updateComment(commentId, command);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        commentService.deleteComment(commentId);
    }
}
