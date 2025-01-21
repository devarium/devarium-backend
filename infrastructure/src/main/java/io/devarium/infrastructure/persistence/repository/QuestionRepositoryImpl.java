package io.devarium.infrastructure.persistence.repository;

import static io.devarium.infrastructure.persistence.entity.QQuestionEntity.questionEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.devarium.core.domain.feedback.exception.FeedbackErrorCode;
import io.devarium.core.domain.feedback.exception.FeedbackException;
import io.devarium.core.domain.feedback.question.Question;
import io.devarium.core.domain.feedback.question.repository.QuestionRepository;
import io.devarium.infrastructure.persistence.entity.ProjectEntity;
import io.devarium.infrastructure.persistence.entity.QuestionEntity;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class QuestionRepositoryImpl implements QuestionRepository {

    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;
    private final QuestionJpaRepository questionJpaRepository;

    @Override
    public Question save(Question question) {
        if (question.getId() != null) {
            QuestionEntity entity = questionJpaRepository.findById(question.getId())
                .orElseThrow(() -> new FeedbackException(
                    FeedbackErrorCode.QUESTION_NOT_FOUND,
                    question.getId()
                ));
            entity.update(question);
            return questionJpaRepository.save(entity).toDomain();
        }

        ProjectEntity project = entityManager.getReference(
            ProjectEntity.class,
            question.getProjectId()
        );
        QuestionEntity entity = QuestionEntity.fromDomain(question, project);

        return questionJpaRepository.save(entity).toDomain();
    }

    @Override
    public List<Question> saveAll(List<Question> questions) {
        List<QuestionEntity> entities = questions.stream()
            .map(question -> {
                if (question.getId() != null) {
                    QuestionEntity entity = questionJpaRepository.findById(question.getId())
                        .orElseThrow(() -> new FeedbackException(
                            FeedbackErrorCode.QUESTION_NOT_FOUND,
                            question.getId()
                        ));
                    entity.update(question);
                    return entity;
                }
                ProjectEntity project = entityManager.getReference(
                    ProjectEntity.class,
                    question.getProjectId()
                );
                return QuestionEntity.fromDomain(question, project);
            })
            .toList();

        return questionJpaRepository.saveAll(entities).stream()
            .map(QuestionEntity::toDomain)
            .toList();
    }

    @Override
    public Optional<Question> findById(Long id) {
        return questionJpaRepository.findById(id).map(QuestionEntity::toDomain);
    }

    @Override
    public List<Question> findAllByProjectId(Long projectId) {
        return questionJpaRepository.findByProjectId(projectId).stream()
            .map(QuestionEntity::toDomain)
            .toList();
    }

    @Override
    public void deleteById(Long id) {
        questionJpaRepository.deleteById(id);
    }

    @Override
    public void deleteAllById(Set<Long> ids) {
        questionJpaRepository.deleteAllById(ids);
    }

    @Override
    public void incrementOrderNumbersFrom(Long projectId, int fromOrder) {
        queryFactory
            .update(questionEntity)
            .set(questionEntity.orderNumber, questionEntity.orderNumber.add(1))
            .where(
                questionEntity.project.id.eq(projectId)
                    .and(questionEntity.orderNumber.goe(fromOrder))
            )
            .execute();
    }
}
