package io.devarium.core.domain.reply.service;

import io.devarium.core.domain.reply.Reply;
import io.devarium.core.domain.reply.command.UpsertReplyCommand;

public interface ReplyService {

    Reply createReply(UpsertReplyCommand command);

    Reply getReply(Long replyId);

    Reply updateReply(Long replyId, UpsertReplyCommand command);

    void deleteReply(Long replyId);
}
