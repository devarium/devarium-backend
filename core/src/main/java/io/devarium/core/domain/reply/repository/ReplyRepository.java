package io.devarium.core.domain.reply.repository;

import io.devarium.core.domain.reply.Reply;
import java.util.Optional;

public interface ReplyRepository {

    Reply save(Reply reply);

    void deleteById(Long replyId);

    void deleteRepliesByCommentId(Long commentId);

    void deleteRepliesByPostId(Long postId);

    Optional<Reply> findById(Long id);
}
