package io.devarium.core.domain.feedback.question.port;

import java.util.List;

public interface SyncQuestions {

    List<? extends SyncQuestion> questions();
}
