package io.devarium.core.domain.reply.port.out;

import io.devarium.core.domain.reply.Reply;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReplyRepository {

    Reply save(Reply reply);

    void deleteById(Long id);

    void deleteByCommentId(Long commentId);

    void deleteByPostId(Long postId);

    Optional<Reply> findById(Long id);

    Page<Reply> findByCommentId(Long commentId, Pageable pageable);
}
