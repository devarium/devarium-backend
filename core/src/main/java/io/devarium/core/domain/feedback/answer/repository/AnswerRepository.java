package io.devarium.core.domain.feedback.answer.repository;

import io.devarium.core.domain.feedback.answer.Answer;
import java.util.Collection;
import java.util.List;

public interface AnswerRepository {

    List<Answer> saveAll(List<Answer> answers);

    List<Answer> findAllByQuestionIdIn(List<Long> questionIds);

    List<Answer> findAllByUserIdAndQuestionIdIn(Long userId, Collection<Long> questionIds);
}
