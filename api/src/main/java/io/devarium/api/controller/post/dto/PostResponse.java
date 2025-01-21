package io.devarium.api.controller.post.dto;

import io.devarium.core.domain.post.Post;
import java.time.Instant;

public record PostResponse(
    Long postId,
    String title,
    String content,
    Long userId,
    Instant createdAt,
    Long likeCount,
    Boolean userLiked
) {

    public static PostResponse from(Post post, Long likeCount, Boolean userLiked) {
        return new PostResponse(
            post.getId(),
            post.getTitle(),
            post.getContent(),
            post.getUserId(),
            post.getCreatedAt(),
            likeCount,
            userLiked
        );
    }
}
