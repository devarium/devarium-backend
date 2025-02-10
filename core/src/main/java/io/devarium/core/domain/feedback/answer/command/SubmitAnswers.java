package io.devarium.core.domain.feedback.answer.command;

import java.util.List;

public interface SubmitAnswers {

    List<? extends SubmitAnswer> answers();

    interface SubmitAnswer {

        Long questionId();

        String content();
    }
}
