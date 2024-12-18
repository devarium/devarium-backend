package io.devarium.core.domain.reply.repository;

import io.devarium.core.domain.reply.Reply;
import java.util.Optional;

public interface ReplyRepository {

    Reply save(Reply reply);

    void deleteById(Long id);

    Optional<Reply> findById(Long id);
}
