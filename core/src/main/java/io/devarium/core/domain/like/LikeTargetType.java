package io.devarium.core.domain.like;

import io.devarium.core.domain.like.exception.LikeErrorCode;
import io.devarium.core.domain.like.exception.LikeException;
import lombok.Getter;

@Getter
public enum LikeTargetType {
    POST,
    COMMENT,
    REPLY;

    public static LikeTargetType fromString(String type) {
        try {
            return LikeTargetType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new LikeException(LikeErrorCode.TARGET_TYPE_NOT_FOUND, type);
        }
    }
}
