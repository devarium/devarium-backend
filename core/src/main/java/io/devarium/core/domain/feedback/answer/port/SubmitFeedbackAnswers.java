package io.devarium.core.domain.feedback.answer.port;

import java.util.List;

public interface SubmitFeedbackAnswers {

    List<? extends SubmitFeedbackAnswer> answers();
}
