package io.devarium.core.domain.feedback.port.out;

import io.devarium.core.domain.feedback.question.Question;
import java.util.List;
import java.util.Optional;

public interface QuestionRepository {

    Question save(Question question);

    List<Question> saveAll(List<Question> questions);

    Optional<Question> findById(Long id);

    List<Question> findAllByProjectId(Long projectId);

    void deleteById(Long id);

    void incrementOrderNumbersFrom(Long projectId, int fromOrder);
}
