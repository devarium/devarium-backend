package io.devarium.api.controller.reply.dto;

import io.devarium.core.domain.reply.Reply;
import java.time.Instant;

public record ReplyResponse(Long replyId, Instant createdAt, String content, Long commentId) {

    public static ReplyResponse from(Reply reply) {
        return new ReplyResponse(
            reply.getId(),
            reply.getCreatedAt(),
            reply.getContent(),
            reply.getCommentId()
        );
    }
}
