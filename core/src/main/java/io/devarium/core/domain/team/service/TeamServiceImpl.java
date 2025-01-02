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
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    @Override
    public Team createTeam(UpsertTeam request, User user) {
        Set<Long> memberIds = Set.of(user.getId());
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
    public Page<Team> getTeams(Pageable pageable, User user) {
        return teamRepository.findByMembers_Id(user.getId(), pageable);
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
    public Team createMembers(Long teamId, CreateMembers request, User user) {
        Team team = getTeam(teamId);
        team.validateLeader(user.getId());
        team.addMembers(request.memberIds());
        return teamRepository.save(team);
    }

    @Override
    public Team deleteMembers(Long teamId, DeleteMembers request, User user) {
        Team team = getTeam(teamId);
        team.validateLeader(user.getId());
        team.removeMembers(request.memberIds());
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
