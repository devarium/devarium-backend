package io.devarium.infrastructure.persistence.service;

import io.devarium.core.domain.comment.Comment;
import io.devarium.core.domain.comment.port.UpsertComment;
import io.devarium.core.domain.comment.service.CommentService;
import io.devarium.core.domain.comment.service.CommentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class CommentServiceDecorator implements CommentService {

    private final CommentServiceImpl commentService;

    @Override
    @Transactional
    public Comment createComment(UpsertComment request) {
        return commentService.createComment(request);
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
    public Comment updateComment(Long commentId, UpsertComment request) {
        return commentService.updateComment(commentId, request);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        commentService.deleteComment(commentId);
    }
}
