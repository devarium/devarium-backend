package io.devarium.core.domain.feedback.question;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;

@Converter
@RequiredArgsConstructor
public class QuestionContentConverter implements AttributeConverter<QuestionContent, String> {

    private final ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(QuestionContent content) {
        try {
            return objectMapper.writeValueAsString(content);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting content to JSON", e);
        }
    }

    @Override
    public QuestionContent convertToEntityAttribute(String json) {
        try {
            return objectMapper.readValue(json, QuestionContent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JSON to content", e);
        }
    }
}
