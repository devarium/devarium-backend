package io.devarium.infrastructure.persistence.repository;

import io.devarium.core.domain.feedback.answer.Answer;
import io.devarium.core.domain.feedback.answer.repository.AnswerRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class AnswerRepositoryImpl implements AnswerRepository {

    private final EntityManager entityManager;
    private final AnswerJpaRepository answerJpaRepository;

    @Override
    public List<Answer> saveAll(List<Answer> answers) {
        return null;
    }

    @Override
    public List<Answer> findAllByQuestionIdIn(List<Long> questionIds) {
        return null;
    }

    @Override
    public void deleteAllByQuestionIdIn(Set<Long> questionIds) {
        
    }
}
