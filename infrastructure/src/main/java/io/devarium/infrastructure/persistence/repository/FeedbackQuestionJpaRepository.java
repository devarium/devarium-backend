package io.devarium.infrastructure.persistence.repository;

import io.devarium.infrastructure.persistence.entity.FeedbackQuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackQuestionJpaRepository extends JpaRepository<FeedbackQuestionEntity, Long> {

}
