package io.devarium.core.domain.feedback.answer.port;

import java.util.List;

public interface SubmitAnswers {

    List<? extends SubmitAnswer> answers();
}
