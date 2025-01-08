package io.devarium.infrastructure.persistence.repository;

import io.devarium.core.domain.feedback.answer.repository.FeedbackAnswerRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class FeedbackAnswerRepositoryImpl implements FeedbackAnswerRepository {

    private final EntityManager entityManager;
    private final FeedbackAnswerJpaRepository feedbackAnswerJpaRepository;
}
