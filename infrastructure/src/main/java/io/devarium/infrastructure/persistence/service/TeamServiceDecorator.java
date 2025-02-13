package io.devarium.infrastructure.persistence.service;

import io.devarium.core.domain.team.Team;
import io.devarium.core.domain.team.command.CreateTeam;
import io.devarium.core.domain.team.command.UpdateLeader;
import io.devarium.core.domain.team.command.UpdateTeamInfo;
import io.devarium.core.domain.team.command.UpdateTeamName;
import io.devarium.core.domain.team.port.in.TeamService;
import io.devarium.core.domain.team.service.TeamServiceImpl;
import io.devarium.core.domain.user.User;
import io.devarium.core.storage.Image;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class TeamServiceDecorator implements TeamService {

    private final TeamServiceImpl teamService;

    @Override
    @Transactional
    public Team createTeam(CreateTeam request, Image image, User user) {
        return teamService.createTeam(request, image, user);
    }

    @Override
    @Transactional(readOnly = true)
    public Team getTeam(Long teamId) {
        return teamService.getTeam(teamId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Team> getTeams(Pageable pageable, String teamName) {
        return teamService.getTeams(pageable, teamName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Team> getTeams(List<Long> teamIds) {
        return teamService.getTeams(teamIds);
    }

    @Override
    @Transactional
    public Team updateTeamInfo(Long teamId, UpdateTeamInfo request, User user) {
        return teamService.updateTeamInfo(teamId, request, user);
    }

    @Override
    @Transactional
    public Team updateTeamName(Long teamId, UpdateTeamName request, User user) {
        return teamService.updateTeamName(teamId, request, user);
    }

    @Override
    @Transactional
    public Team updateTeamProfileImage(Long teamId, Image image, User user) {
        return teamService.updateTeamProfileImage(teamId, image, user);
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

    @Override
    @Transactional(readOnly = true)
    public boolean checkUserIsLeader(Long userId) {
        return teamService.checkUserIsLeader(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkTeamExists(Long teamId) {
        return teamService.checkTeamExists(teamId);
    }
}
