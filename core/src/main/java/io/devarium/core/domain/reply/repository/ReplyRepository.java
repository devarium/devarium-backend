package io.devarium.core.domain.reply.repository;

import io.devarium.core.domain.reply.Reply;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReplyRepository {

    Reply save(Reply reply);

    void deleteById(Long id);

    void deleteRepliesByCommentId(Long commentId);

    void deleteRepliesByPostId(Long postId);

    Optional<Reply> findById(Long id);

    Page<Reply> findByCommentId(Long postId, Pageable pageable);
}
