package io.devarium.core.domain.reply.service;

import io.devarium.core.domain.reply.Reply;
import io.devarium.core.domain.reply.port.UpsertReply;

public interface ReplyService {

    Reply createReply(UpsertReply request);

    Reply getReply(Long replyId);

    Reply updateReply(Long replyId, UpsertReply request);

    void deleteReply(Long replyId);
}
