package io.devarium.infrastructure.persistence.service;

import io.devarium.core.domain.team.Team;
import io.devarium.core.domain.team.port.CreateMembers;
import io.devarium.core.domain.team.port.DeleteMembers;
import io.devarium.core.domain.team.port.UpdateLeader;
import io.devarium.core.domain.team.port.UpsertTeam;
import io.devarium.core.domain.team.service.TeamService;
import io.devarium.core.domain.team.service.TeamServiceImpl;
import io.devarium.core.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public Page<Team> getTeams(User user) {
        return teamService.getTeams(user);
    }

    @Override
    @Transactional
    public Team updateTeam(UpsertTeam request, User user) {
        return teamService.updateTeam(request, user);
    }

    @Override
    @Transactional
    public Team updateLeader(UpdateLeader request, User user) {
        return teamService.updateLeader(request, user);
    }

    @Override
    @Transactional
    public Team createMembers(CreateMembers request, User user) {
        return teamService.createMembers(request, user);
    }

    @Override
    @Transactional
    public Team deleteMembers(DeleteMembers request, User user) {
        return teamService.deleteMembers(request, user);
    }

    @Override
    @Transactional
    public void deleteTeam(Long teamId, User user) {
        teamService.deleteTeam(teamId, user);
    }
}
