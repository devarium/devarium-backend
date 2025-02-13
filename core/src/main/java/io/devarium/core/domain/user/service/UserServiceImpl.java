package io.devarium.core.domain.user.service;

import io.devarium.core.auth.OAuth2UserInfo;
import io.devarium.core.domain.membership.Membership;
import io.devarium.core.domain.membership.port.in.MembershipService;
import io.devarium.core.domain.team.Team;
import io.devarium.core.domain.team.port.in.TeamService;
import io.devarium.core.domain.teamRequest.TeamRequest;
import io.devarium.core.domain.teamRequest.TeamRequestStatus;
import io.devarium.core.domain.teamRequest.TeamRequestType;
import io.devarium.core.domain.teamRequest.port.in.TeamRequestService;
import io.devarium.core.domain.user.User;
import io.devarium.core.domain.user.UserRole;
import io.devarium.core.domain.user.command.UpdateUser;
import io.devarium.core.domain.user.exception.UserErrorCode;
import io.devarium.core.domain.user.exception.UserException;
import io.devarium.core.domain.user.port.in.UserService;
import io.devarium.core.domain.user.port.out.UserRepository;
import io.devarium.core.storage.Image;
import io.devarium.core.storage.ImageType;
import io.devarium.core.storage.port.in.StorageService;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final StorageService storageService;
    private final TeamService teamService;
    private final MembershipService membershipService;
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
    public List<User> getUsers(Set<Long> userIds) {
        return userRepository.findAllById(userIds);
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
            .map(Membership::getTeamId)
            .toList();
        return teamService.getTeams(teamIds);
    }

    @Override
    public List<Membership> getMemberships(User user) {
        return membershipService.getMemberships(user.getId());
    }

    @Override
    public List<TeamRequest> getTeamRequests(
        TeamRequestType type,
        List<TeamRequestStatus> status,
        User user
    ) {
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
