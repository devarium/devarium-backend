package io.devarium.core.domain.feedback.answer.repository;

import io.devarium.core.domain.feedback.answer.Answer;
import java.util.List;
import java.util.Set;

public interface AnswerRepository {

    List<Answer> saveAll(List<Answer> answers);

    List<Answer> findAllByQuestionIdIn(List<Long> questionIds);

    void deleteAllByQuestionIdIn(Set<Long> questionIds);
}
