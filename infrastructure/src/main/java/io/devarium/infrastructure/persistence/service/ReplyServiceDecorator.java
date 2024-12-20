package io.devarium.infrastructure.persistence.service;

import io.devarium.core.domain.reply.Reply;
import io.devarium.core.domain.reply.port.UpsertReply;
import io.devarium.core.domain.reply.service.ReplyService;
import io.devarium.core.domain.reply.service.ReplyServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class ReplyServiceDecorator implements ReplyService {

    private final ReplyServiceImpl replyService;

    @Override
    @Transactional
    public Reply createReply(UpsertReply request) {
        return replyService.createReply(request);
    }

    @Override
    @Transactional(readOnly = true)
    public Reply getReply(Long replyId) {
        return replyService.getReply(replyId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Reply> getRepliesByCommentId(Long commentId, Pageable pageable) {
        return replyService.getRepliesByCommentId(commentId, pageable);
    }

    @Override
    @Transactional
    public Reply updateReply(Long replyId, UpsertReply request) {
        return replyService.updateReply(replyId, request);
    }

    @Override
    @Transactional
    public void deleteReply(Long replyId) {
        replyService.deleteReply(replyId);
    }
}
