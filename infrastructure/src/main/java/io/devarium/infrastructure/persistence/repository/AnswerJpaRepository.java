package io.devarium.infrastructure.persistence.repository;

import io.devarium.infrastructure.persistence.entity.AnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerJpaRepository extends JpaRepository<AnswerEntity, Long> {

}
