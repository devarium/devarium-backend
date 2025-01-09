package io.devarium.infrastructure.persistence.repository;

import io.devarium.core.domain.feedback.question.repository.QuestionRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class QuestionRepositoryImpl implements QuestionRepository {

    private final EntityManager entityManager;
    private final QuestionJpaRepository questionJpaRepository;
}
