package io.devarium.core.domain.comment.service;

import io.devarium.core.domain.comment.Comment;
import io.devarium.core.domain.comment.exception.CommentErrorCode;
import io.devarium.core.domain.comment.exception.CommentException;
import io.devarium.core.domain.comment.port.UpsertComment;
import io.devarium.core.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public Comment createComment(UpsertComment request) {
        Comment comment = Comment.builder()
            .content(request.content())
            .postId(request.postId())
            .build();
        return commentRepository.save(comment);
    }

    @Override
    public Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
            .orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND, commentId));
    }

    @Override
    public Comment updateComment(Long commentId, UpsertComment request) {
        Comment comment = getComment(commentId);
        comment.updateContent(request.content());
        return commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Long commentId) {
        Comment comment = getComment(commentId);
        commentRepository.delete(comment);
    }
}
