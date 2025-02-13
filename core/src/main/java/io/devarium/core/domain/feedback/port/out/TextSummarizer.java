package io.devarium.core.domain.feedback.port.out;

import java.util.List;

public interface TextSummarizer {

    String summarizeTexts(List<String> texts);
}
