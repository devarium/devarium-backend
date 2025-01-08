package io.devarium.infrastructure.persistence.repository;

import io.devarium.core.domain.feedback.question.repository.FeedbackQuestionRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class FeedbackQuestionRepositoryImpl implements FeedbackQuestionRepository {

    private final EntityManager entityManager;
    private final FeedbackQuestionJpaRepository feedbackQuestionJpaRepository;
}
