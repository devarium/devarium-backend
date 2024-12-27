package io.devarium.api.controller.reply.dto;

import io.devarium.core.domain.reply.Reply;
import java.time.Instant;

public record ReplyResponse(Long replyId, String content, Long commentId, Instant createdAt) {

    public static ReplyResponse from(Reply reply) {
        return new ReplyResponse(
            reply.getId(),
            reply.getContent(),
            reply.getCommentId(),
            reply.getCreatedAt()
        );
    }
}
