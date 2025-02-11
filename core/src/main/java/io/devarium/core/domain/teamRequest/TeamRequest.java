package io.devarium.core.domain.teamRequest;

import io.devarium.core.domain.teamRequest.exception.TeamRequestErrorCode;
import io.devarium.core.domain.teamRequest.exception.TeamRequestException;
import java.time.Instant;
import java.util.Objects;
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
        if (this.status == TeamRequestStatus.ACCEPTED) {
            throw new TeamRequestException(
                TeamRequestErrorCode.TEAM_REQUEST_ALREADY_ACCEPTED,
                this.id,
                status
            );
        }
        this.status = status;
        this.statusChangedAt = Instant.now();
    }

    public void validateUser(Long userId) {
        if (!Objects.equals(this.userId, userId)) {
            throw new TeamRequestException(
                TeamRequestErrorCode.TEAM_REQUEST_NOT_FOUND_BY_USER,
                id,
                userId
            );
        }
    }
}
