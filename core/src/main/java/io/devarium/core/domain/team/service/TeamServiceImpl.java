package io.devarium.core.domain.team.service;

import io.devarium.core.domain.membership.MemberRole;
import io.devarium.core.domain.membership.port.in.MembershipService;
import io.devarium.core.domain.team.Team;
import io.devarium.core.domain.team.command.CreateTeam;
import io.devarium.core.domain.team.command.UpdateLeader;
import io.devarium.core.domain.team.command.UpdateTeamInfo;
import io.devarium.core.domain.team.command.UpdateTeamName;
import io.devarium.core.domain.team.exception.TeamErrorCode;
import io.devarium.core.domain.team.exception.TeamException;
import io.devarium.core.domain.team.port.in.TeamService;
import io.devarium.core.domain.team.port.out.TeamRepository;
import io.devarium.core.domain.user.User;
import io.devarium.core.storage.Image;
import io.devarium.core.storage.ImageType;
import io.devarium.core.storage.port.in.StorageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final MembershipService membershipService;
    private final StorageService storageService;

    @Override
    public Team createTeam(CreateTeam request, Image image, User user) {
        // TODO: 프로필 이미지 없을 시 디폴트 이미지 사용
        String profileImageUrl = storageService.upload(image, ImageType.PROFILE);
        Team team = Team.builder()
            .name(request.name())
            .description(request.description())
            .profileImageUrl(profileImageUrl)
            .githubUrl(request.githubUrl())
            .leaderId(user.getId())
            .build();
        Team savedTeam = teamRepository.save(team);
        membershipService.createLeader(savedTeam.getId(), user.getId());
        return savedTeam;
    }

    @Override
    public Team getTeam(Long teamId) {
        return teamRepository.findById(teamId)
            .orElseThrow(() -> new TeamException(TeamErrorCode.TEAM_NOT_FOUND, teamId));
    }

    @Override
    public Page<Team> getTeams(Pageable pageable, String teamName) {
        return teamRepository.findByNameContaining(teamName, pageable);
    }

    @Override
    public List<Team> getTeams(List<Long> teamIds) {
        return teamRepository.findAllById(teamIds);
    }

    @Override
    public Team updateTeamInfo(Long teamId, UpdateTeamInfo request, User user) {
        membershipService.getMembership(teamId, user.getId()).validateRole(MemberRole.MANAGER);
        Team team = getTeam(teamId);
        team.updateInfo(request.description(), request.githubUrl());
        return teamRepository.save(team);
    }

    @Override
    public Team updateTeamName(Long teamId, UpdateTeamName request, User user) {
        Team team = getTeam(teamId);
        team.validateLeader(user.getId());
        team.updateName(request.name());
        return teamRepository.save(team);
    }

    @Override
    public Team updateTeamProfileImage(Long teamId, Image image, User user) {
        Team team = getTeam(teamId);
        team.validateLeader(user.getId());
        // TODO: 디폴트 이미지가 아닌 경우 delete 실행
        String profileImageUrl = team.getProfileImageUrl();
        if (profileImageUrl != null) {
            storageService.delete(profileImageUrl);
        }
        String newImageUrl = storageService.upload(image, ImageType.PROFILE);
        team.updateProfileImage(newImageUrl);
        return teamRepository.save(team);
    }

    @Override
    public Team updateLeader(Long teamId, UpdateLeader request, User user) {
        Team team = getTeam(teamId);
        team.validateLeader(user.getId());
        team.updateLeader(request.leaderId());
        membershipService.updateLeader(teamId, user.getId(), team.getLeaderId());
        return teamRepository.save(team);
    }

    @Override
    public void deleteTeam(Long teamId, User user) {
        Team team = getTeam(teamId);
        team.validateLeader(user.getId());
        team.delete();
        teamRepository.save(team);
    }

    @Override
    public boolean checkUserIsLeader(Long userId) {
        return teamRepository.existsByLeaderId(userId);
    }

    @Override
    public boolean checkTeamExists(Long teamId) {
        return teamRepository.existsById(teamId);
    }
}
