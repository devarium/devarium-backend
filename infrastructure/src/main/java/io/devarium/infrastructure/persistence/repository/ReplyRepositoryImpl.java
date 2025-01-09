package io.devarium.infrastructure.persistence.repository;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.devarium.core.domain.comment.exception.CommentErrorCode;
import io.devarium.core.domain.comment.exception.CommentException;
import io.devarium.core.domain.reply.Reply;
import io.devarium.core.domain.reply.exception.ReplyErrorCode;
import io.devarium.core.domain.reply.exception.ReplyException;
import io.devarium.core.domain.reply.repository.ReplyRepository;
import io.devarium.infrastructure.persistence.entity.CommentEntity;
import io.devarium.infrastructure.persistence.entity.QCommentEntity;
import io.devarium.infrastructure.persistence.entity.QReplyEntity;
import io.devarium.infrastructure.persistence.entity.ReplyEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ReplyRepositoryImpl implements ReplyRepository {

    private final ReplyJpaRepository replyJpaRepository;
    private final CommentJpaRepository commentJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Reply save(Reply reply) {
        ReplyEntity entity = convertToEntity(reply);
        ReplyEntity savedEntity = replyJpaRepository.save(entity);
        return convertToDomain(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        QReplyEntity reply = QReplyEntity.replyEntity;
        queryFactory.delete(reply)
            .where(reply.id.eq(id))
            .execute();
    }

    @Override
    public void deleteByCommentId(Long commentId) {
        QReplyEntity reply = QReplyEntity.replyEntity;
        queryFactory.delete(reply)
            .where(reply.comment.id.eq(commentId))
            .execute();
    }

    @Override
    public void deleteByPostId(Long postId) {
        QReplyEntity reply = QReplyEntity.replyEntity;
        QCommentEntity comment = QCommentEntity.commentEntity;
        queryFactory.delete(reply)
            .where(reply.comment.id.in(
                JPAExpressions.select(comment.id)
                    .from(comment)
                    .where(comment.post.id.eq(postId))
            ))
            .execute();
    }

    @Override
    public Optional<Reply> findById(Long id) {
        return replyJpaRepository.findById(id).map(this::convertToDomain);
    }

    @Override
    public Page<Reply> findByCommentId(Long commentId, Pageable pageable) {
        return replyJpaRepository.findByCommentId(commentId, pageable).map(this::convertToDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return replyJpaRepository.existsById(id);
    }

    private Reply convertToDomain(ReplyEntity entity) {
        return Reply.builder()
            .id(entity.getId())
            .content(entity.getContent())
            .createdAt(entity.getCreatedAt())
            .commentId(entity.getComment().getId())
            .build();
    }

    private ReplyEntity convertToEntity(Reply domain) {
        if (domain.getId() != null) {
            ReplyEntity entity = replyJpaRepository.findById(domain.getId())
                .orElseThrow(
                    () -> new ReplyException(ReplyErrorCode.REPLY_NOT_FOUND, domain.getId()));
            entity.update(domain);
            return entity;
        }

        CommentEntity comment = commentJpaRepository.findById(domain.getCommentId())
            .orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND,
                domain.getCommentId()));
        return ReplyEntity.builder()
            .content(domain.getContent())
            .comment(comment)
            .build();
    }
}
