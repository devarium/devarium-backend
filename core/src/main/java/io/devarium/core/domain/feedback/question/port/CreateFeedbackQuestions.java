package io.devarium.core.domain.feedback.question.port;

import java.util.List;

public interface CreateFeedbackQuestions {

    List<? extends CreateFeedbackQuestion> questions();
}
