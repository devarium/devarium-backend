package io.devarium.core.domain.like;

import io.devarium.core.domain.like.exception.LikeErrorCode;
import io.devarium.core.domain.like.exception.LikeException;
import lombok.Getter;

@Getter
public enum EntityType {
    POST,
    COMMENT,
    REPLY;

    public static EntityType fromString(String type) {
        try {
            return EntityType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new LikeException(LikeErrorCode.ENTITY_TYPE_NOT_FOUND, type);
        }
    }
}
