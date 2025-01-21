package io.devarium.api.controller.reply.dto;

import io.devarium.core.domain.reply.Reply;
import java.time.Instant;

public record ReplyResponse(
    Long replyId,
    String content,
    Long commentId,
    Long userId,
    Instant createdAt,
    Long likeCount,
    Boolean userLiked
) {

    public static ReplyResponse from(Reply reply, Long likeCount, Boolean userLiked) {
        return new ReplyResponse(
            reply.getId(),
            reply.getContent(),
            reply.getCommentId(),
            reply.getUserId(),
            reply.getCreatedAt(),
            likeCount,
            userLiked
        );
    }
}
