package io.devarium.core.domain.teamRequest.repository;

import io.devarium.core.domain.teamRequest.TeamRequest;
import io.devarium.core.domain.teamRequest.TeamRequestStatus;
import io.devarium.core.domain.teamRequest.TeamRequestType;
import java.util.List;

public interface TeamRequestRepository {

    TeamRequest save(TeamRequest teamRequest);

    List<TeamRequest> saveAll(List<TeamRequest> teamRequests);

    List<TeamRequest> findByTeamIdAndTypeAndStatus(
        Long teamId,
        TeamRequestType type,
        TeamRequestStatus status
    );

    List<TeamRequest> findByIdInAndTeamId(Long teamId, List<Long> ids);
}
