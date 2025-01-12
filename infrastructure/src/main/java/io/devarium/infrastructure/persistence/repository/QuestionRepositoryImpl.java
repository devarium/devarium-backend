package io.devarium.infrastructure.persistence.repository;

import io.devarium.core.domain.feedback.question.Question;
import io.devarium.core.domain.feedback.question.repository.QuestionRepository;
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
    private final QuestionJpaRepository questionJpaRepository;

    @Override
    public List<Question> saveAll(List<Question> questions) {
        return null;
    }

    @Override
    public Optional<Question> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Question> findAllByProjectId(Long projectId) {
        return null;
    }

    @Override
    public void deleteAllById(Set<Long> ids) {

    }
}
