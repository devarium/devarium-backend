package io.devarium.infrastructure.persistence.service;

import io.devarium.core.domain.comment.Comment;
import io.devarium.core.domain.comment.command.UpsertComment;
import io.devarium.core.domain.comment.port.in.CommentService;
import io.devarium.core.domain.comment.service.CommentServiceImpl;
import io.devarium.core.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class CommentServiceDecorator implements CommentService {

    private final CommentServiceImpl commentService;

    @Override
    @Transactional
    public Comment createComment(UpsertComment request, User user) {
        return commentService.createComment(request, user);
    }

    @Override
    @Transactional(readOnly = true)
    public Comment getComment(Long commentId) {
        return commentService.getComment(commentId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Comment> getCommentsByPostId(Long postId, Pageable pageable) {
        return commentService.getCommentsByPostId(postId, pageable);
    }

    @Override
    @Transactional
    public Comment updateComment(Long commentId, UpsertComment request, User user) {
        return commentService.updateComment(commentId, request, user);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, User user) {
        commentService.deleteComment(commentId, user);
    }

    @Override
    @Transactional
    public void deleteCommentsByPostId(Long postId) {
        commentService.deleteCommentsByPostId(postId);
    }
}
