package io.devarium.core.domain.reply.repository;

import io.devarium.core.domain.reply.Reply;
import java.util.Optional;

public interface ReplyRepository {

    Reply save(Reply reply);

    void delete(Reply reply);

    Optional<Reply> findById(Long id);
}
