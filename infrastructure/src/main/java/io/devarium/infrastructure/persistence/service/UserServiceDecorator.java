package io.devarium.infrastructure.persistence.service;

import io.devarium.core.auth.OAuth2UserInfo;
import io.devarium.core.domain.member.Member;
import io.devarium.core.domain.team.Team;
import io.devarium.core.domain.teamRequest.TeamRequest;
import io.devarium.core.domain.teamRequest.TeamRequestStatus;
import io.devarium.core.domain.teamRequest.TeamRequestType;
import io.devarium.core.domain.user.User;
import io.devarium.core.domain.user.port.UpdateUser;
import io.devarium.core.domain.user.service.UserService;
import io.devarium.core.domain.user.service.UserServiceImpl;
import io.devarium.core.storage.Image;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class UserServiceDecorator implements UserService {

    private final UserServiceImpl userService;

    @Override
    @Transactional
    public User createUser(OAuth2UserInfo userInfo) {
        return userService.createUser(userInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(Long userId) {
        return userService.getUser(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public User getByEmail(String email) {
        return userService.getByEmail(email);
    }

    @Override
    @Transactional
    public User updateProfile(UpdateUser request, User user) {
        return userService.updateProfile(request, user);
    }

    @Override
    @Transactional
    public User updateProfileImage(Image image, User user) {
        return userService.updateProfileImage(image, user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Team> getTeams(User user) {
        return userService.getTeams(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Member> getMemberships(User user) {
        return userService.getMemberships(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamRequest> getTeamRequests(
        TeamRequestType type,
        TeamRequestStatus status,
        User user
    ) {
        return userService.getTeamRequests(type, status, user);
    }

    @Override
    @Transactional
    public TeamRequest updateInvitation(Long teamRequestId, TeamRequestStatus status, User user) {
        return userService.updateInvitation(teamRequestId, status, user);
    }

    @Override
    @Transactional
    public void withdraw(User user) {
        userService.withdraw(user);
    }
}
