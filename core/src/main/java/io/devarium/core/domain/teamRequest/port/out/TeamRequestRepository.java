package io.devarium.core.domain.teamRequest.port.out;

import io.devarium.core.domain.teamRequest.TeamRequest;
import io.devarium.core.domain.teamRequest.TeamRequestStatus;
import io.devarium.core.domain.teamRequest.TeamRequestType;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TeamRequestRepository {

    TeamRequest save(TeamRequest teamRequest);

    List<TeamRequest> saveAll(
        List<TeamRequest> createdTeamRequests,
        List<TeamRequest> updatedTeamRequests
    );

    Optional<TeamRequest> findById(Long id);

    Optional<TeamRequest> findByTeamIdAndUserIdAndType(
        Long teamId,
        Long userId,
        TeamRequestType type
    );

    List<TeamRequest> findAllByTeamIdAndUserIdInAndType(
        Long teamId,
        Set<Long> userIds,
        TeamRequestType type
    );

    List<TeamRequest> findAllByTeamIdAndTypeAndStatusIn(
        Long teamId,
        TeamRequestType type,
        List<TeamRequestStatus> status
    );

    List<TeamRequest> findAllByUserIdAndTypeAndStatusIn(
        Long userId,
        TeamRequestType type,
        List<TeamRequestStatus> status
    );

    List<TeamRequest> findAllByIdInAndTeamId(Long teamId, Set<Long> ids);
}
