package io.devarium.core.domain.teamRequest.service;

import io.devarium.core.domain.teamRequest.TeamRequest;
import io.devarium.core.domain.teamRequest.TeamRequestStatus;
import io.devarium.core.domain.teamRequest.TeamRequestType;
import io.devarium.core.domain.teamRequest.exception.TeamRequestErrorCode;
import io.devarium.core.domain.teamRequest.exception.TeamRequestException;
import io.devarium.core.domain.teamRequest.port.CreateInvitation;
import io.devarium.core.domain.teamRequest.port.UpdateJoin;
import io.devarium.core.domain.teamRequest.repository.TeamRequestRepository;
import io.devarium.core.domain.user.User;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TeamRequestServiceImpl implements TeamRequestService {

    private final TeamRequestRepository teamRequestRepository;

    @Override
    public TeamRequest join(Long teamId, User user) {
        // TODO: teamId 존재 검증
        // TODO: User의 멤버십 x 검증
        TeamRequest teamRequest = TeamRequest.builder()
            .teamId(teamId)
            .userId(user.getId())
            .type(TeamRequestType.JOIN_REQUEST)
            .status(TeamRequestStatus.PENDING)
            .statusChangedAt(Instant.now())
            .build();
        return teamRequestRepository.save(teamRequest);
    }

    @Override
    public List<TeamRequest> invite(Long teamId, CreateInvitation request, User user) {
        // TODO: User의 멤버십 및 MANAGER 이상 권한 검증
        // TODO: userIds의 멤버십 x 검증
        List<TeamRequest> teamRequests = request.userIds().stream()
            .map(userId -> TeamRequest.builder()
                .teamId(teamId)
                .userId(userId)
                .type(TeamRequestType.INVITATION)
                .status(TeamRequestStatus.PENDING)
                .statusChangedAt(Instant.now())
                .build())
            .toList();
        return teamRequestRepository.saveAll(teamRequests);
    }

    @Override
    public List<TeamRequest> getTeamRequests(Long teamId, TeamRequestType type,
        TeamRequestStatus status, User user) {
        // TODO: User의 멤버십 검증
        // TODO: INVITATION은 매니저 이상
        return teamRequestRepository.findByTeamIdAndTypeAndStatus(teamId, type, status);
    }

    @Override
    public List<TeamRequest> update(
        Long teamId,
        TeamRequestStatus status,
        UpdateJoin request,
        User user
    ) {
        // TODO: User의 멤버십 및 LEADER 권한 검증
        List<TeamRequest> teamRequests = teamRequestRepository.findByIdInAndTeamId(
            teamId,
            request.teamRequestIds()
        );
        if (request.teamRequestIds().size() != teamRequests.size()) {
            Set<Long> teamRequestIds = teamRequests.stream()
                .map(TeamRequest::getId)
                .collect(Collectors.toSet());

            List<Long> unmatchedIds = request.teamRequestIds().stream()
                .filter(id -> !teamRequestIds.contains(id))
                .toList();

            throw new TeamRequestException(
                TeamRequestErrorCode.TEAM_REQUESTS_NOT_FOUND,
                unmatchedIds
            );
        }
        teamRequests.forEach(teamRequest -> teamRequest.update(status));
        return teamRequestRepository.saveAll(teamRequests);
    }
}
