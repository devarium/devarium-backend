package io.devarium.core.domain.teamRequest.port.in;

import io.devarium.core.domain.teamRequest.TeamRequest;
import io.devarium.core.domain.teamRequest.TeamRequestStatus;
import io.devarium.core.domain.teamRequest.TeamRequestType;
import io.devarium.core.domain.teamRequest.command.CreateInvitations;
import io.devarium.core.domain.teamRequest.command.UpdateJoins;
import io.devarium.core.domain.user.User;
import java.util.List;

public interface TeamRequestService {

    TeamRequest join(Long teamId, User user);

    List<TeamRequest> invite(Long teamId, CreateInvitations request, User user);

    List<TeamRequest> getTeamRequests(
        Long teamId,
        TeamRequestType type,
        List<TeamRequestStatus> status,
        User user
    );

    List<TeamRequest> getTeamRequestsByUser(
        TeamRequestType type,
        List<TeamRequestStatus> status,
        User user
    );

    List<TeamRequest> updateJoinRequests(
        Long teamId,
        TeamRequestStatus status,
        UpdateJoins request,
        User user
    );

    TeamRequest updateInvitation(Long teamRequestId, TeamRequestStatus status, User user);
}
