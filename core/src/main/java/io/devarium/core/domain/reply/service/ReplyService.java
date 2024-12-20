package io.devarium.core.domain.reply.service;

import io.devarium.core.domain.reply.Reply;
import io.devarium.core.domain.reply.port.UpsertReply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReplyService {

    Reply createReply(UpsertReply request);

    Reply getReply(Long replyId);

    Page<Reply> getRepliesByCommentId(Long commentId, Pageable pageable);

    Reply updateReply(Long replyId, UpsertReply request);

    void deleteReply(Long replyId);
}
