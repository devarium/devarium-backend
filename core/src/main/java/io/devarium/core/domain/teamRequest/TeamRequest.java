package io.devarium.core.domain.teamRequest;

import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TeamRequest {

    private final Long id;
    private final Long teamId;
    private final Long userId;
    private final TeamRequestType type;
    private TeamRequestStatus status;
    private Instant statusChangedAt;

    @Builder
    public TeamRequest(
        Long id,
        Long teamId,
        Long userId,
        TeamRequestType type,
        TeamRequestStatus status,
        Instant statusChangedAt
    ) {
        this.id = id;
        this.teamId = teamId;
        this.userId = userId;
        this.type = type;
        this.status = status;
        this.statusChangedAt = statusChangedAt;
    }

    public void update(TeamRequestStatus status) {
        this.status = status;
        this.statusChangedAt = Instant.now();
    }
}
