package io.devarium.core.domain.feedback.question.command;

import io.devarium.core.domain.feedback.question.QuestionContent;
import io.devarium.core.domain.feedback.question.QuestionType;

public interface UpdateQuestion {

    QuestionContent content();

    QuestionType type();

    Boolean required();
}
