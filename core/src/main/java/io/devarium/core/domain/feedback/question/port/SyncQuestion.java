package io.devarium.core.domain.feedback.question.port;

import io.devarium.core.domain.feedback.question.QuestionType;

public interface SyncQuestion {

    Long questionId();

    int orderNumber();

    String content();

    QuestionType type();

    Boolean required();
}
