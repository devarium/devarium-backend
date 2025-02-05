package io.devarium.core.domain.feedback.question.port;

import io.devarium.core.domain.feedback.question.QuestionContent;
import io.devarium.core.domain.feedback.question.QuestionType;
import java.util.List;

public interface CreateQuestion {

    int orderNumber();

    QuestionContent questionContent();

    QuestionType type();

    Boolean required();

    interface CreateQuestionContent {

        String content();

        List<? extends CreateChoice> choices();
    }

    interface CreateChoice {

        int orderNumber();

        String label();
    }
}
