package io.devarium.core.domain.feedback.question.port;

import io.devarium.core.domain.feedback.question.QuestionType;

public interface UpdateQuestion {

    Long questionId();

    String content();

    QuestionType type();

    Boolean required();

    int orderNumber();
}
