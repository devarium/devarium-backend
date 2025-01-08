package io.devarium.infrastructure.persistence.repository;

import io.devarium.infrastructure.persistence.entity.FeedbackAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackAnswerJpaRepository extends JpaRepository<FeedbackAnswerEntity, Long> {

}
