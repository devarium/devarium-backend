package io.devarium.api.controller.teamRequest.dto;

import io.devarium.core.domain.teamRequest.TeamRequest;
import io.devarium.core.domain.teamRequest.TeamRequestStatus;
import io.devarium.core.domain.teamRequest.TeamRequestType;
import java.time.Instant;

public record TeamRequestResponse(
    Long id,
    Long teamId,
    Long userId,
    TeamRequestType type,
    TeamRequestStatus status,
    Instant statusChangedAt
) {

    public static TeamRequestResponse from(TeamRequest teamRequest) {
        return new TeamRequestResponse(
            teamRequest.getId(),
            teamRequest.getTeamId(),
            teamRequest.getUserId(),
            teamRequest.getType(),
            teamRequest.getStatus(),
            teamRequest.getStatusChangedAt()
        );
    }
}
