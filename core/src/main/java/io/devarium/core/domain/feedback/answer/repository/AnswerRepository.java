package io.devarium.core.domain.feedback.answer.repository;

import io.devarium.core.domain.feedback.answer.Answer;
import java.util.List;

public interface AnswerRepository {

    List<Answer> saveAll(List<Answer> answers);

    List<Answer> findAllByQuestionIdIn(List<Long> questionIds);
}
