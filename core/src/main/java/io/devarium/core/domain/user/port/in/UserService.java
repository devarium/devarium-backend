package io.devarium.core.domain.user.port.in;

import io.devarium.core.auth.OAuth2UserInfo;
import io.devarium.core.domain.membership.Membership;
import io.devarium.core.domain.team.Team;
import io.devarium.core.domain.teamRequest.TeamRequest;
import io.devarium.core.domain.teamRequest.TeamRequestStatus;
import io.devarium.core.domain.teamRequest.TeamRequestType;
import io.devarium.core.domain.user.User;
import io.devarium.core.domain.user.command.UpdateUser;
import io.devarium.core.storage.Image;
import java.util.List;
import java.util.Set;

public interface UserService {

    User createUser(OAuth2UserInfo userInfo);

    User getUser(Long userId);

    List<User> getUsers(Set<Long> userIds);

    User getByEmail(String email);

    User updateProfile(UpdateUser request, User user);

    User updateProfileImage(Image image, User user);

    List<Team> getTeams(User user);

    List<Membership> getMemberships(User user);

    List<TeamRequest> getTeamRequests(
        TeamRequestType type,
        List<TeamRequestStatus> status,
        User user
    );

    TeamRequest updateInvitation(Long teamRequestId, TeamRequestStatus status, User user);

    void withdraw(User user);
}
