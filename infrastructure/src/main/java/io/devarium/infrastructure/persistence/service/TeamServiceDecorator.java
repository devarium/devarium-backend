package io.devarium.infrastructure.persistence.service;

import io.devarium.core.domain.team.Team;
import io.devarium.core.domain.team.port.UpdateLeader;
import io.devarium.core.domain.team.port.UpsertTeam;
import io.devarium.core.domain.team.service.TeamService;
import io.devarium.core.domain.team.service.TeamServiceImpl;
import io.devarium.core.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @Transactional(readOnly = true)
    public Team getTeam(Long teamId) {
        return teamService.getTeam(teamId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Team> getTeams(Pageable pageable, User user) {
        return teamService.getTeams(pageable, user);
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
