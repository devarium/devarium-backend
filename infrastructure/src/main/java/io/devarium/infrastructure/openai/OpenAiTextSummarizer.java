package io.devarium.infrastructure.openai;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import io.devarium.core.domain.feedback.port.out.TextSummarizer;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

@RequiredArgsConstructor
@Slf4j
@Component
public class OpenAiTextSummarizer implements TextSummarizer {

    private final OpenAiService openAiService;
    private final StringRedisTemplate redisTemplate;

    @Override
    public String summarizeTexts(List<String> texts) {
        String cacheKey = generateCacheKey(texts);
        String cachedSummary = redisTemplate.opsForValue().get("summary:" + cacheKey);

        if (cachedSummary != null) {
            log.info("Cache hit for key: {}", cacheKey);
            return cachedSummary;
        }

        ChatCompletionRequest request = ChatCompletionRequest.builder()
            .model("gpt-3.5-turbo")
            .messages(List.of(
                new ChatMessage(
                    "user",
                    "다음 텍스트들을 요약해주세요:\n" + String.join("\n", texts)
                )
            ))
            .build();

        log.info("Sending request to OpenAI: {}", request);

        ChatCompletionResult response = openAiService.createChatCompletion(request);
        log.info("Received response from OpenAI: {}", response);

        String summary = response.getChoices()
            .getFirst()
            .getMessage()
            .getContent();

        redisTemplate.opsForValue().set("summary:" + cacheKey, summary, 24, TimeUnit.HOURS);

        return summary;
    }

    private String generateCacheKey(List<String> texts) {
        return DigestUtils.md5DigestAsHex(String.join("", texts).getBytes());
    }
}
