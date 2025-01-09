package io.devarium.infrastructure.persistence.repository;

import io.devarium.infrastructure.persistence.entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionJpaRepository extends JpaRepository<QuestionEntity, Long> {

}
