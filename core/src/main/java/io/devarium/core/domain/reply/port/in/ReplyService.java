package io.devarium.core.domain.reply.port.in;

import io.devarium.core.domain.reply.Reply;
import io.devarium.core.domain.reply.command.UpsertReply;
import io.devarium.core.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReplyService {

    Reply createReply(UpsertReply request, User user);

    Reply getReply(Long replyId);

    Page<Reply> getRepliesByCommentId(Long commentId, Pageable pageable);

    Reply updateReply(Long replyId, UpsertReply request, User user);

    void deleteReply(Long replyId, User user);

    void deleteRepliesByCommentId(Long commentId);

    void deleteRepliesByPostId(Long postId);
}
