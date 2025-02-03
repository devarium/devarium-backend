package io.devarium.core.domain.teamRequest.service;

import io.devarium.core.domain.teamRequest.TeamRequest;
import io.devarium.core.domain.teamRequest.TeamRequestStatus;
import io.devarium.core.domain.teamRequest.TeamRequestType;
import io.devarium.core.domain.teamRequest.port.CreateInvitation;
import io.devarium.core.domain.teamRequest.port.UpdateJoin;
import io.devarium.core.domain.user.User;
import java.util.List;

public interface TeamRequestService {

    TeamRequest join(Long teamId, User user);

    List<TeamRequest> invite(Long teamId, CreateInvitation request, User user);

    List<TeamRequest> getTeamRequests(
        Long teamId,
        TeamRequestType type,
        TeamRequestStatus status,
        User user
    );

    List<TeamRequest> update(
        Long teamId,
        TeamRequestStatus status,
        UpdateJoin request,
        User user
    );
}
