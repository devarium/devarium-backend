package io.devarium.infrastructure.persistence.service;

import io.devarium.core.domain.teamRequest.TeamRequest;
import io.devarium.core.domain.teamRequest.TeamRequestStatus;
import io.devarium.core.domain.teamRequest.TeamRequestType;
import io.devarium.core.domain.teamRequest.port.CreateInvitation;
import io.devarium.core.domain.teamRequest.port.UpdateJoin;
import io.devarium.core.domain.teamRequest.service.TeamRequestService;
import io.devarium.core.domain.teamRequest.service.TeamRequestServiceImpl;
import io.devarium.core.domain.user.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class TeamRequestServiceDecorator implements TeamRequestService {

    private final TeamRequestServiceImpl teamRequestService;

    @Override
    @Transactional
    public TeamRequest join(Long teamId, User user) {
        return teamRequestService.join(teamId, user);
    }

    @Override
    @Transactional
    public List<TeamRequest> invite(Long teamId, CreateInvitation request, User user) {
        return teamRequestService.invite(teamId, request, user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamRequest> getTeamRequests(
        Long teamId,
        TeamRequestType type,
        TeamRequestStatus status,
        User user
    ) {
        return teamRequestService.getTeamRequests(teamId, type, status, user);
    }

    @Override
    @Transactional
    public List<TeamRequest> update(
        Long teamId,
        TeamRequestStatus status,
        UpdateJoin request,
        User user
    ) {
        return teamRequestService.update(teamId, status, request, user);
    }
}
