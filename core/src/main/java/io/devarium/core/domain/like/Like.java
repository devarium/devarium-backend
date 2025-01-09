package io.devarium.core.domain.like;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Like {

    private final Long id;
    private final EntityType entityType;
    private final Long entityId;
    private final Long userId;

    @Builder
    private Like(Long id, EntityType entityType, Long entityId, Long userId) {
        this.id = id;
        this.entityType = entityType;
        this.entityId = entityId;
        this.userId = userId;
    }
}
