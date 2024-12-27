package io.devarium.core.domain.comment.service;

import io.devarium.core.domain.comment.Comment;
import io.devarium.core.domain.comment.exception.CommentErrorCode;
import io.devarium.core.domain.comment.exception.CommentException;
import io.devarium.core.domain.comment.port.UpsertComment;
import io.devarium.core.domain.comment.repository.CommentRepository;
import io.devarium.core.domain.reply.service.ReplyService;
import io.devarium.core.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ReplyService replyService;

    @Override
    public Comment createComment(UpsertComment request, User user) {
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
    public Page<Comment> getCommentsByPostId(Long postId, Pageable pageable) {
        return commentRepository.findByPostId(postId, pageable);
    }

    @Override
    public Comment updateComment(Long commentId, UpsertComment request, User user) {
        Comment comment = getComment(commentId);
        comment.updateContent(request.content());
        return commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Long commentId, User user) {
        replyService.deleteRepliesByCommentId(commentId);
        commentRepository.deleteById(commentId);
    }

    @Override
    public void deleteCommentsByPostId(Long postId) {
        replyService.deleteRepliesByPostId(postId);
        commentRepository.deleteByPostId(postId);
    }
}
