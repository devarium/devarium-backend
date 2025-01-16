package io.devarium.core.domain.feedback.port;

import java.util.List;

public interface TextSummarizer {

    String summarizeTexts(List<String> texts);
}
