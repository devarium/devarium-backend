package io.devarium.infrastructure.persistence.repository;

import io.devarium.core.domain.feedback.answer.repository.AnswerRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class AnswerRepositoryImpl implements AnswerRepository {

    private final EntityManager entityManager;
    private final AnswerJpaRepository answerJpaRepository;
}
