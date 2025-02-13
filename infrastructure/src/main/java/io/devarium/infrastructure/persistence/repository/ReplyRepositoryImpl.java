package io.devarium.infrastructure.persistence.repository;

import io.devarium.core.domain.reply.Reply;
import io.devarium.core.domain.reply.exception.ReplyErrorCode;
import io.devarium.core.domain.reply.exception.ReplyException;
import io.devarium.core.domain.reply.port.out.ReplyRepository;
import io.devarium.infrastructure.persistence.entity.CommentEntity;
import io.devarium.infrastructure.persistence.entity.ReplyEntity;
import io.devarium.infrastructure.persistence.entity.UserEntity;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ReplyRepositoryImpl implements ReplyRepository {

    private final EntityManager entityManager;
    private final ReplyJpaRepository replyJpaRepository;

    @Override
    public Reply save(Reply reply) {
        if (reply.getId() != null) {
            ReplyEntity entity = replyJpaRepository.findById(reply.getId())
                .orElseThrow(() -> new ReplyException(
                    ReplyErrorCode.REPLY_NOT_FOUND,
                    reply.getId())
                );
            entity.update(reply);
            return replyJpaRepository.save(entity).toDomain();
        }

        CommentEntity comment = entityManager.getReference(
            CommentEntity.class,
            reply.getCommentId()
        );
        UserEntity user = entityManager.getReference(UserEntity.class, reply.getUserId());

        ReplyEntity entity = ReplyEntity.fromDomain(reply, comment, user);
        return replyJpaRepository.save(entity).toDomain();
    }

    @Override
    public void deleteById(Long id) {
        replyJpaRepository.deleteById(id);
    }

    @Override
    public void deleteByCommentId(Long commentId) {
        replyJpaRepository.deleteByCommentId(commentId);
    }

    @Override
    public void deleteByPostId(Long postId) {
        replyJpaRepository.deleteByCommentPostId(postId);
    }

    @Override
    public Optional<Reply> findById(Long id) {
        return replyJpaRepository.findById(id).map(ReplyEntity::toDomain);
    }

    @Override
    public Page<Reply> findByCommentId(Long commentId, Pageable pageable) {
        return replyJpaRepository.findByCommentId(commentId, pageable).map(ReplyEntity::toDomain);
    }
}
