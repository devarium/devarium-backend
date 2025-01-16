package io.devarium.infrastructure.openai;

import com.theokanning.openai.service.OpenAiService;
import io.devarium.core.domain.feedback.port.TextSummarizer;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OpenAiTextSummarizer implements TextSummarizer {

    private final OpenAiService openAiService;

    @Override
    public String summarizeTexts(List<String> texts) {
        return null;
    }
}
