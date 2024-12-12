package io.devarium.core.domain.reply.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReplyErrorCode {
    REPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "Reply not found with id: %d");

    private final HttpStatus status;
    private final String message;

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
