package io.devarium.infrastructure.persistence.service;

import io.devarium.core.domain.team.Team;
import io.devarium.core.domain.team.port.UpdateLeader;
import io.devarium.core.domain.team.port.UpsertTeam;
import io.devarium.core.domain.team.service.TeamService;
import io.devarium.core.domain.team.service.TeamServiceImpl;
import io.devarium.core.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class TeamServiceDecorator implements TeamService {

    private final TeamServiceImpl teamService;

    @Override
    @Transactional
    public Team createTeam(UpsertTeam request, User user) {
        return teamService.createTeam(request, user);
    }

    @Override
    @Transactional
    public void initializeTeam(Long teamId, Long userId) {
        teamService.initializeTeam(teamId, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Team getTeam(Long teamId) {
        return teamService.getTeam(teamId);
    }

    @Override
    @Transactional
    public Team updateTeam(Long teamId, UpsertTeam request, User user) {
        return teamService.updateTeam(teamId, request, user);
    }

    @Override
    @Transactional
    public Team updateLeader(Long teamId, UpdateLeader request, User user) {
        return teamService.updateLeader(teamId, request, user);
    }

    @Override
    @Transactional
    public void deleteTeam(Long teamId, User user) {
        teamService.deleteTeam(teamId, user);
    }
}
