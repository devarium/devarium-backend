package io.devarium.core.domain.comment.port.in;

import io.devarium.core.domain.comment.Comment;
import io.devarium.core.domain.comment.command.UpsertComment;
import io.devarium.core.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    Comment createComment(UpsertComment request, User user);

    Comment getComment(Long commentId);

    Page<Comment> getCommentsByPostId(Long postId, Pageable pageable);

    Comment updateComment(Long commentId, UpsertComment request, User user);

    void deleteComment(Long commentId, User user);

    void deleteCommentsByPostId(Long postId);
}
