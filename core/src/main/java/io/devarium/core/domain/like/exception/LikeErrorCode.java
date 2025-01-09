package io.devarium.core.domain.like.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum LikeErrorCode {
    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "Like not found with id: %d"),
    ENTITY_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "EntityType not found with type: %s"),
    ENTITY_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "EntityId not found with id: %d")
    ;

    private final HttpStatus status;
    private final String message;

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
