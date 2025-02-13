package io.devarium.core.domain.team.service;

import io.devarium.core.domain.member.port.in.MemberService;
import io.devarium.core.domain.team.Team;
import io.devarium.core.domain.team.command.UpdateLeader;
import io.devarium.core.domain.team.command.UpsertTeam;
import io.devarium.core.domain.team.exception.TeamErrorCode;
import io.devarium.core.domain.team.exception.TeamException;
import io.devarium.core.domain.team.port.in.TeamService;
import io.devarium.core.domain.team.port.out.TeamRepository;
import io.devarium.core.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final MemberService memberService;

    @Override
    public Team createTeam(UpsertTeam request, User user) {
        Team team = Team.builder()
            .name(request.name())
            .description(request.description())
            .picture(request.picture())
            .githubUrl(request.githubUrl())
            .leaderId(user.getId())
            .build();
        Team savedTeam = teamRepository.save(team);
        memberService.createLeader(savedTeam.getId(), user.getId());
        return savedTeam;
    }

    @Override
    public Team getTeam(Long teamId) {
        return teamRepository.findById(teamId)
            .orElseThrow(() -> new TeamException(TeamErrorCode.TEAM_NOT_FOUND, teamId));
    }

    @Override
    public Page<Team> getTeams(Pageable pageable, User user) {
        return teamRepository.findAllByUserId(user.getId(), pageable);
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

        memberService.updateLeader(teamId, user.getId(), team.getLeaderId());
        return teamRepository.save(team);
    }

    @Override
    public void deleteTeam(Long teamId, User user) {
        Team team = getTeam(teamId);
        team.validateLeader(user.getId());
        team.delete();
        teamRepository.save(team);
    }
}
