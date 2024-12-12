package io.devarium.core.domain.reply.service;

import io.devarium.core.domain.reply.Reply;
import io.devarium.core.domain.reply.command.UpsertReplyCommand;
import io.devarium.core.domain.reply.exception.ReplyErrorCode;
import io.devarium.core.domain.reply.exception.ReplyException;
import io.devarium.core.domain.reply.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;

    @Override
    public Reply createReply(UpsertReplyCommand command) {
        Reply reply = Reply.builder()
            .content(command.content())
            .build();
        return replyRepository.save(reply);
    }

    @Override
    public Reply getReply(Long replyId) {
        return replyRepository.findById(replyId)
            .orElseThrow(() -> new ReplyException(ReplyErrorCode.REPLY_NOT_FOUND, replyId));
    }

    @Override
    public Reply updateReply(Long replyId, UpsertReplyCommand command) {
        Reply reply = getReply(replyId);
        reply.updateContent(command.content());
        return replyRepository.save(reply);
    }

    @Override
    public void deleteReply(Long replyId) {
        replyRepository.deleteById(replyId);
    }
}
