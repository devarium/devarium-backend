package io.devarium.core.domain.feedback.question.port;

import io.devarium.core.domain.feedback.question.QuestionContent;
import io.devarium.core.domain.feedback.question.QuestionType;

public interface CreateQuestion {

    int orderNumber();

    QuestionContent content();

    QuestionType type();

    Boolean required();
}
