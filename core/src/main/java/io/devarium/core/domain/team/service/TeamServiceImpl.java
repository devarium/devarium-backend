package io.devarium.core.domain.team.service;

import io.devarium.core.domain.team.Team;
import io.devarium.core.domain.team.exception.TeamErrorCode;
import io.devarium.core.domain.team.exception.TeamException;
import io.devarium.core.domain.team.port.UpdateLeader;
import io.devarium.core.domain.team.port.UpsertTeam;
import io.devarium.core.domain.team.repository.TeamRepository;
import io.devarium.core.domain.user.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    @Override
    public Team createTeam(UpsertTeam request, User user) {
        Team team = Team.builder()
            .name(request.name())
            .description(request.description())
            .picture(request.picture())
            .githubUrl(request.githubUrl())
            .leaderId(user.getId())
            .build();
        return teamRepository.save(team);
    }

    @Override
    public Team updateTeam(Long teamId, UpsertTeam request, User user) {
        Team team = getTeam(teamId);
        team.validateLeader(user.getId());
        team.update(
            request.name(),
            request.description(),
            request.picture(),
            request.githubUrl()
        );
        return teamRepository.save(team);
    }

    @Override
    public Team updateLeader(Long teamId, UpdateLeader request, User user) {
        Team team = getTeam(teamId);
        team.validateLeader(user.getId());
        team.updateLeader(request.leaderId());
        return teamRepository.save(team);
    }

    @Override
    public void deleteTeam(Long teamId, User user) {
        Team team = getTeam(teamId);
        team.validateLeader(user.getId());
        team.delete();
        teamRepository.save(team);
    }

    private Team getTeam(Long teamId) {
        return teamRepository.findById(teamId)
            .orElseThrow(() -> new TeamException(TeamErrorCode.TEAM_NOT_FOUND, teamId));
    }
}
