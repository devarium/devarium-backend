package io.devarium.core.domain.user.service;

import io.devarium.core.auth.OAuth2UserInfo;
import io.devarium.core.domain.member.Member;
import io.devarium.core.domain.member.service.MemberService;
import io.devarium.core.domain.team.Team;
import io.devarium.core.domain.team.service.TeamService;
import io.devarium.core.domain.teamRequest.TeamRequest;
import io.devarium.core.domain.teamRequest.TeamRequestStatus;
import io.devarium.core.domain.teamRequest.TeamRequestType;
import io.devarium.core.domain.teamRequest.service.TeamRequestService;
import io.devarium.core.domain.user.User;
import io.devarium.core.domain.user.UserRole;
import io.devarium.core.domain.user.exception.UserErrorCode;
import io.devarium.core.domain.user.exception.UserException;
import io.devarium.core.domain.user.port.UpdateUser;
import io.devarium.core.domain.user.repository.UserRepository;
import io.devarium.core.storage.Image;
import io.devarium.core.storage.ImageType;
import io.devarium.core.storage.service.StorageService;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final StorageService storageService;
    private final TeamService teamService;
    private final MemberService memberService;
    private final TeamRequestService teamRequestService;

    @Override
    public User createUser(OAuth2UserInfo userInfo) {
        User user = User.builder()
            .email(userInfo.email())
            .username(userInfo.name())
            .profileImageUrl(userInfo.profileImageUrl())
            .provider(userInfo.provider())
            .role(UserRole.USER)
            .build();
        return userRepository.save(user);
    }

    @Override
    public User getUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND, userId));
        if (user.isDeleted()) {
            throw new UserException(UserErrorCode.USER_IS_DELETED, user.getId());
        }
        return user;
    }

    @Override
    public User getByEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserException(UserErrorCode.USER_EMAIL_NOT_FOUND, email));
        if (user.isDeleted()) {
            throw new UserException(UserErrorCode.USER_IS_DELETED, user.getId());
        }
        return user;
    }

    @Override
    public User updateProfile(UpdateUser request, User user) {
        user.update(request.username(), request.bio(), request.blogUrl(), request.githubUrl());
        return userRepository.save(user);
    }

    @Override
    public User updateProfileImage(Image image, User user) {
        storageService.delete(user.getProfileImageUrl());
        String newImageUrl = storageService.upload(image, ImageType.PROFILE);
        user.update(newImageUrl);
        return userRepository.save(user);
    }

    @Override
    public List<Team> getTeams(User user) {
        List<Long> teamIds = getMemberships(user).stream()
            .map(Member::getTeamId)
            .toList();
        return teamService.getTeams(teamIds);
    }

    @Override
    public List<Member> getMemberships(User user) {
        return memberService.getMembers(user.getId());
    }

    @Override
    public List<TeamRequest> getTeamRequests(TeamRequestType type, TeamRequestStatus status,
        User user) {
        return teamRequestService.getTeamRequestsByUser(type, status, user);
    }

    @Override
    public TeamRequest updateInvitation(Long teamRequestId, TeamRequestStatus status, User user) {
        return teamRequestService.updateInvitation(teamRequestId, status, user);
    }

    @Override
    public void withdraw(User user) {
        if (teamService.checkUserIsLeader(user.getId())) {
            throw new UserException(UserErrorCode.USER_WITHDRAW_FAILED, user.getId());
        }
        user.delete();
        userRepository.save(user);
    }
}
