package io.devarium.api.controller.comment.dto;

import io.devarium.core.domain.comment.Comment;
import java.time.Instant;

public record CommentResponse(
    Long commentId,
    String content,
    Long postId,
    Long userId,
    Instant createdAt,
    Long likeCount,
    Boolean userLiked
) {

    public static CommentResponse from(Comment comment, Long likeCount, Boolean userLiked) {
        return new CommentResponse(
            comment.getId(),
            comment.getContent(),
            comment.getPostId(),
            comment.getUserId(),
            comment.getCreatedAt(),
            likeCount,
            userLiked
        );
    }
}
