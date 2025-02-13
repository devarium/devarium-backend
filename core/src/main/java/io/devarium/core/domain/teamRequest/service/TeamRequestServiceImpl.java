package io.devarium.core.domain.teamRequest.service;

import io.devarium.core.domain.membership.MemberRole;
import io.devarium.core.domain.membership.Membership;
import io.devarium.core.domain.membership.exception.MembershipException;
import io.devarium.core.domain.membership.port.in.MembershipService;
import io.devarium.core.domain.team.port.in.TeamService;
import io.devarium.core.domain.teamRequest.TeamRequest;
import io.devarium.core.domain.teamRequest.TeamRequestStatus;
import io.devarium.core.domain.teamRequest.TeamRequestType;
import io.devarium.core.domain.teamRequest.command.CreateInvitations;
import io.devarium.core.domain.teamRequest.command.UpdateJoins;
import io.devarium.core.domain.teamRequest.exception.TeamRequestErrorCode;
import io.devarium.core.domain.teamRequest.exception.TeamRequestException;
import io.devarium.core.domain.teamRequest.port.in.TeamRequestService;
import io.devarium.core.domain.teamRequest.port.out.TeamRequestRepository;
import io.devarium.core.domain.user.User;
import io.devarium.core.domain.user.port.in.UserService;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TeamRequestServiceImpl implements TeamRequestService {

    private final TeamRequestRepository teamRequestRepository;
    private final MembershipService membershipService;
    private final TeamService teamService;
    private final UserService userService;

    @Override
    public TeamRequest join(Long teamId, User user) {
        if (!teamService.checkTeamExists(teamId)) {
            throw new TeamRequestException(TeamRequestErrorCode.TEAM_NOT_FOUND, teamId);
        }
        try {
            Membership membership = membershipService.getMembership(teamId, user.getId());
            throw new TeamRequestException(
                TeamRequestErrorCode.MEMBERSHIP_ALREADY_EXISTS,
                user.getId(),
                membership.getId(),
                teamId
            );
        } catch (MembershipException e) {
            TeamRequest teamRequest = teamRequestRepository.findByTeamIdAndUserIdAndType(
                teamId,
                user.getId(),
                TeamRequestType.JOIN_REQUEST
            ).orElseGet(() -> TeamRequest.builder()
                .teamId(teamId)
                .userId(user.getId())
                .type(TeamRequestType.JOIN_REQUEST)
                .build()
            );
            teamRequest.update(TeamRequestStatus.PENDING);
            return teamRequestRepository.save(teamRequest);
        }
    }

    @Override
    public List<TeamRequest> invite(Long teamId, CreateInvitations request, User user) {
        if (!teamService.checkTeamExists(teamId)) {
            throw new TeamRequestException(TeamRequestErrorCode.TEAM_NOT_FOUND, teamId);
        }
        Set<Long> requestUserIds = request.userIds();
        List<User> users = userService.getUsers(requestUserIds);
        if (users.size() != requestUserIds.size()) {
            List<Long> userIds = users.stream().map(User::getId).toList();
            List<Long> unmatchedIds = requestUserIds.stream()
                .filter(id -> !userIds.contains(id))
                .toList();
            throw new TeamRequestException(
                TeamRequestErrorCode.USERS_NOT_FOUND,
                unmatchedIds
            );
        }
        membershipService.getMembership(teamId, user.getId()).validateRole(MemberRole.MANAGER);
        List<Membership> memberships = membershipService.getMemberships(teamId, requestUserIds);
        if (!memberships.isEmpty()) {
            throw new TeamRequestException(
                TeamRequestErrorCode.MEMBERSHIPS_ALREADY_EXIST,
                memberships.stream().map(Membership::getUserId).toList(),
                teamId
            );
        }

        List<TeamRequest> teamRequests = teamRequestRepository.findAllByTeamIdAndUserIdInAndType(
            teamId,
            requestUserIds,
            TeamRequestType.INVITATION
        );
        List<TeamRequest> updatedTeamRequests = teamRequests.stream()
            .peek(teamRequest -> teamRequest.update(TeamRequestStatus.PENDING)).toList();
        if (!teamRequests.isEmpty()) {
            List<Long> userIds = teamRequests.stream().map(TeamRequest::getUserId).toList();
            requestUserIds = requestUserIds.stream()
                .filter(id -> !userIds.contains(id))
                .collect(Collectors.toSet());
        }
        List<TeamRequest> createdTeamRequests = requestUserIds.stream()
            .map(userId -> TeamRequest.builder()
                .teamId(teamId)
                .userId(userId)
                .type(TeamRequestType.INVITATION)
                .status(TeamRequestStatus.PENDING)
                .statusChangedAt(Instant.now())
                .build())
            .toList();
        return teamRequestRepository.saveAll(createdTeamRequests, updatedTeamRequests);
    }

    @Override
    public List<TeamRequest> getTeamRequests(
        Long teamId,
        TeamRequestType type,
        List<TeamRequestStatus> status,
        User user
    ) {
        if (!teamService.checkTeamExists(teamId)) {
            throw new TeamRequestException(TeamRequestErrorCode.TEAM_NOT_FOUND, teamId);
        }
        Membership membership = membershipService.getMembership(teamId, user.getId());
        if (type == TeamRequestType.INVITATION) {
            membership.validateRole(MemberRole.MANAGER);
        }
        return teamRequestRepository.findAllByTeamIdAndTypeAndStatusIn(teamId, type, status);
    }

    @Override
    public List<TeamRequest> getTeamRequestsByUser(
        TeamRequestType type,
        List<TeamRequestStatus> status,
        User user
    ) {
        return teamRequestRepository.findAllByUserIdAndTypeAndStatusIn(user.getId(), type, status);
    }

    @Override
    public List<TeamRequest> updateJoinRequests(
        Long teamId,
        TeamRequestStatus status,
        UpdateJoins request,
        User user
    ) {
        if (!teamService.checkTeamExists(teamId)) {
            throw new TeamRequestException(TeamRequestErrorCode.TEAM_NOT_FOUND, teamId);
        }
        membershipService.getMembership(teamId, user.getId()).validateRole(MemberRole.LEADER);
        List<TeamRequest> teamRequests = teamRequestRepository.findAllByIdInAndTeamId(
            teamId,
            request.ids()
        );
        if (teamRequests.size() != request.ids().size()) {
            List<Long> teamRequestIds = teamRequests.stream()
                .map(TeamRequest::getId)
                .toList();

            List<Long> unmatchedIds = request.ids().stream()
                .filter(id -> !teamRequestIds.contains(id))
                .toList();

            throw new TeamRequestException(
                TeamRequestErrorCode.TEAM_REQUESTS_NOT_FOUND,
                unmatchedIds
            );
        }

        teamRequests.forEach(teamRequest -> teamRequest.update(status));
        if (status == TeamRequestStatus.ACCEPTED) {
            List<Long> userIds = teamRequests.stream()
                .map(TeamRequest::getUserId)
                .toList();
            membershipService.createMemberships(teamId, userIds);
        }
        return teamRequestRepository.saveAll(null, teamRequests);
    }

    @Override
    public TeamRequest updateInvitation(Long teamRequestId, TeamRequestStatus status, User user) {
        TeamRequest teamRequest = teamRequestRepository.findById(teamRequestId)
            .orElseThrow(() -> new TeamRequestException(
                TeamRequestErrorCode.TEAM_REQUEST_NOT_FOUND,
                teamRequestId)
            );
        teamRequest.validateUser(user.getId());
        teamRequest.update(status);
        if (status == TeamRequestStatus.ACCEPTED) {
            membershipService.createMemberships(
                teamRequest.getTeamId(),
                List.of(user.getId())
            );
        }
        return teamRequestRepository.save(teamRequest);
    }
}
