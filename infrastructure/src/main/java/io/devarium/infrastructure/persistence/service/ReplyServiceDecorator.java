package io.devarium.infrastructure.persistence.service;

import io.devarium.core.domain.reply.Reply;
import io.devarium.core.domain.reply.command.UpsertReplyCommand;
import io.devarium.core.domain.reply.service.ReplyService;
import io.devarium.core.domain.reply.service.ReplyServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class ReplyServiceDecorator implements ReplyService {

    private final ReplyServiceImpl replyService;


    @Override
    @Transactional
    public Reply createReply(UpsertReplyCommand command) {
        return replyService.createReply(command);
    }

    @Override
    @Transactional(readOnly = true)
    public Reply getReply(Long replyId) {
        return replyService.getReply(replyId);
    }

    @Override
    @Transactional
    public Reply updateReply(Long replyId, UpsertReplyCommand command) {
        return replyService.updateReply(replyId, command);
    }

    @Override
    @Transactional
    public void deleteReply(Long replyId) {
        replyService.deleteReply(replyId);
    }
}
