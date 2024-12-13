package io.devarium.infrastructure.persistence.repository;

import io.devarium.core.domain.reply.Reply;
import io.devarium.core.domain.reply.exception.ReplyErrorCode;
import io.devarium.core.domain.reply.exception.ReplyException;
import io.devarium.core.domain.reply.repository.ReplyRepository;
import io.devarium.infrastructure.persistence.entity.ReplyEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ReplyRepositoryImpl implements ReplyRepository {

    private final ReplyJpaRepository replyJpaRepository;

    @Override
    public Reply save(Reply reply) {
        ReplyEntity entity = convertToEntity(reply);
        ReplyEntity savedEntity = replyJpaRepository.save(entity);
        return convertToDomain(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        replyJpaRepository.deleteById(id);
    }

    @Override
    public Optional<Reply> findById(Long id) {
        return replyJpaRepository.findById(id).map(this::convertToDomain);
    }

    private Reply convertToDomain(ReplyEntity entity) {
        return Reply.builder()
            .id(entity.getId())
            .content(entity.getContent())
            .createdAt(entity.getCreatedAt())
            .build();
    }

    private ReplyEntity convertToEntity(Reply domain) {
        if (domain.getId() != null) {
            ReplyEntity entity = replyJpaRepository.findById(domain.getId())
                .orElseThrow(() -> new ReplyException(ReplyErrorCode.REPLY_NOT_FOUND, domain.getId()));
            entity.update(domain);
            return entity;
        }

        return ReplyEntity.builder()
            .content(domain.getContent())
            .build();
    }
}
