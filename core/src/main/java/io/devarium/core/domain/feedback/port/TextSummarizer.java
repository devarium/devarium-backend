package io.devarium.core.domain.feedback.port;

import io.devarium.core.domain.feedback.QuestionWithAnswers;

public interface TextSummarizer {

    String summarizeAnswers(QuestionWithAnswers questionWithAnswers);
}
