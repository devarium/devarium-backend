package io.devarium.core.domain.team.service;

import io.devarium.core.domain.team.Team;
import io.devarium.core.domain.team.exception.TeamErrorCode;
import io.devarium.core.domain.team.exception.TeamException;
import io.devarium.core.domain.team.port.CreateMembers;
import io.devarium.core.domain.team.port.DeleteMembers;
import io.devarium.core.domain.team.port.UpdateLeader;
import io.devarium.core.domain.team.port.UpsertTeam;
import io.devarium.core.domain.team.repository.TeamRepository;
import io.devarium.core.domain.user.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    @Override
    public Team createTeam(UpsertTeam request, User user) {
        List<Long> memberIds = List.of(user.getId());
        Team team = Team.builder()
            .name(request.name())
            .description(request.description())
            .picture(request.picture())
            .githubUrl(request.githubUrl())
            .leaderId(user.getId())
            .memberIds(memberIds)
            .build();
        return teamRepository.save(team);
    }

    @Override
    public Team getTeam(Long teamId) {
        return teamRepository.findById(teamId)
            .orElseThrow(() -> new TeamException(TeamErrorCode.TEAM_NOT_FOUND, teamId));
    }

    @Override
    public Page<Team> getTeams(User user) {
        return null;
    }

    @Override
    public Team updateTeam(UpsertTeam request, User user) {
        return null;
    }

    @Override
    public Team updateLeader(UpdateLeader request, User user) {
        return null;
    }

    @Override
    public Team createMembers(CreateMembers request, User user) {
        return null;
    }

    @Override
    public Team deleteMembers(DeleteMembers request, User user) {
        return null;
    }

    @Override
    public void deleteTeam(Long teamId, User user) {
        Team team = getTeam(teamId);
        team.validateLeader(user.getId());
        team.delete();
        teamRepository.save(team);
    }
}
