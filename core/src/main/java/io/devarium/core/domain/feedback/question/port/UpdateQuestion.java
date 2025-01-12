package io.devarium.core.domain.feedback.question.port;

public interface UpdateQuestion {

    Long questionId();

    String content();

    Boolean required();

    int orderNumber();
}
