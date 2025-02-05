package io.devarium.core.domain.team.service;

import io.devarium.core.domain.member.service.MemberService;
import io.devarium.core.domain.team.Team;
import io.devarium.core.domain.team.exception.TeamErrorCode;
import io.devarium.core.domain.team.exception.TeamException;
import io.devarium.core.domain.team.port.CreateTeam;
import io.devarium.core.domain.team.port.UpdateLeader;
import io.devarium.core.domain.team.port.UpdateTeamInfo;
import io.devarium.core.domain.team.port.UpdateTeamName;
import io.devarium.core.domain.team.repository.TeamRepository;
import io.devarium.core.domain.user.User;
import io.devarium.core.storage.Image;
import io.devarium.core.storage.ImageType;
import io.devarium.core.storage.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final MemberService memberService;
    private final StorageService storageService;

    @Override
    public Team createTeam(CreateTeam request, User user) {
        // TODO: 프로필 이미지 없을 시 디폴트 이미지 사용
        Team team = Team.builder()
            .name(request.name())
            .description(request.description())
            .profileImageUrl(request.profileImageUrl())
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
    public Page<Team> getTeams(Pageable pageable, String teamName) {
        return teamRepository.findByNameContaining(teamName, pageable);
    }

    @Override
    public Team updateTeamInfo(Long teamId, UpdateTeamInfo request, User user) {
        // TODO: User - MANAGER 권한 이상
        Team team = getTeam(teamId);
        team.updateInfo(
            request.description(),
            request.githubUrl()
        );
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
