package io.devarium.infrastructure.persistence.repository;

import io.devarium.core.domain.feedback.answer.Answer;
import io.devarium.core.domain.feedback.answer.repository.AnswerRepository;
import io.devarium.core.domain.feedback.exception.FeedbackErrorCode;
import io.devarium.core.domain.feedback.exception.FeedbackException;
import io.devarium.infrastructure.persistence.entity.AnswerEntity;
import io.devarium.infrastructure.persistence.entity.QuestionEntity;
import io.devarium.infrastructure.persistence.entity.UserEntity;
import jakarta.persistence.EntityManager;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class AnswerRepositoryImpl implements AnswerRepository {

    private final EntityManager entityManager;
    private final AnswerJpaRepository answerJpaRepository;

    @Override
    public List<Answer> saveAll(List<Answer> answers) {
        List<AnswerEntity> entities = answers.stream()
            .map(answer -> {
                if (answer.getId() != null) {
                    AnswerEntity entity = answerJpaRepository.findById(answer.getId())
                        .orElseThrow(() -> new FeedbackException(
                            FeedbackErrorCode.ANSWER_NOT_FOUND,
                            answer.getId())
                        );
                    entity.update(answer);
                    return entity;
                }
                QuestionEntity question = entityManager.getReference(
                    QuestionEntity.class,
                    answer.getQuestionId()
                );
                UserEntity user = entityManager.getReference(
                    UserEntity.class,
                    answer.getUserId()
                );
                return AnswerEntity.fromDomain(answer, question, user);
            })
            .toList();

        return answerJpaRepository.saveAll(entities).stream()
            .map(AnswerEntity::toDomain)
            .toList();
    }

    @Override
    public List<Answer> findAllByQuestionIdIn(List<Long> questionIds) {
        return answerJpaRepository.findAllByQuestionIdIn(questionIds)
            .stream()
            .map(AnswerEntity::toDomain)
            .toList();
    }

    @Override
    public List<Answer> findAllByUserIdAndQuestionIdIn(Long userId, Collection<Long> questionIds) {
        return answerJpaRepository.findAllByUserIdAndQuestionIdIn(userId, questionIds)
            .stream()
            .map(AnswerEntity::toDomain)
            .toList();
    }
}
