package io.devarium.infrastructure.persistence.service;

import io.devarium.core.domain.reply.Reply;
import io.devarium.core.domain.reply.command.UpsertReply;
import io.devarium.core.domain.reply.port.in.ReplyService;
import io.devarium.core.domain.reply.service.ReplyServiceImpl;
import io.devarium.core.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class ReplyServiceDecorator implements ReplyService {

    private final ReplyServiceImpl replyService;

    @Override
    @Transactional
    public Reply createReply(UpsertReply request, User user) {
        return replyService.createReply(request, user);
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
    public Reply updateReply(Long replyId, UpsertReply request, User user) {
        return replyService.updateReply(replyId, request, user);
    }

    @Override
    @Transactional
    public void deleteReply(Long replyId, User user) {
        replyService.deleteReply(replyId, user);
    }

    @Override
    @Transactional
    public void deleteRepliesByCommentId(Long commentId) {
        replyService.deleteRepliesByCommentId(commentId);
    }

    @Override
    @Transactional
    public void deleteRepliesByPostId(Long postId) {
        replyService.deleteRepliesByPostId(postId);
    }
}
