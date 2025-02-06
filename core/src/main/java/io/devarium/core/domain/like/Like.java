package io.devarium.core.domain.like;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.tool.schema.TargetType;

@Getter
public class Like {

    private final Long id;
    private final LikeTargetType targetType;
    private final Long targetId;
    private final Long userId;

    @Builder
    private Like(Long id, LikeTargetType targetType, Long targetId, Long userId) {
        this.id = id;
        this.targetType = targetType;
        this.targetId = targetId;
        this.userId = userId;
    }
}
