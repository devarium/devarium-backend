package io.devarium.infrastructure.persistence.repository;

import io.devarium.infrastructure.persistence.entity.AnswerEntity;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerJpaRepository extends JpaRepository<AnswerEntity, Long> {

    Collection<AnswerEntity> findAllByQuestionIdIn(Collection<Long> questionIds);

    void deleteAllByQuestionIdIn(Collection<Long> questionIds);
}
