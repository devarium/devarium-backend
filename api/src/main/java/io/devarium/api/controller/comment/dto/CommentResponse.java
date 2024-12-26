package io.devarium.api.controller.comment.dto;

import io.devarium.core.domain.comment.Comment;
import java.time.Instant;

public record CommentResponse(Long commentId, Instant createdAt, String content, Long postId) {

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
            comment.getId(),
            comment.getCreatedAt(),
            comment.getContent(),
            comment.getPostId()
        );
    }
}
