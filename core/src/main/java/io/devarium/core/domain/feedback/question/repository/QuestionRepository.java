package io.devarium.core.domain.feedback.question.repository;

import io.devarium.core.domain.feedback.question.Question;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface QuestionRepository {

    Question save(Question question);

    List<Question> saveAll(List<Question> questions);

    Optional<Question> findById(Long id);

    List<Question> findAllByProjectId(Long projectId);

    void deleteAllById(Set<Long> ids);

    void deleteById(Long id);

    void incrementOrderNumbersFrom(Long projectId, int fromOrder);
}
