package io.devarium.core.domain.feedback.question.port;

import io.devarium.core.domain.feedback.question.QuestionType;

public interface UpdateQuestion {

    String content();

    QuestionType type();

    Boolean required();
}
