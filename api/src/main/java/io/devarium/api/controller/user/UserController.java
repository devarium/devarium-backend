package io.devarium.api.controller.user;

import io.devarium.api.auth.CustomUserPrincipal;
import io.devarium.api.common.dto.ListResponse;
import io.devarium.api.common.dto.SingleItemResponse;
import io.devarium.api.controller.membership.dto.MembershipResponse;
import io.devarium.api.controller.team.dto.TeamResponse;
import io.devarium.api.controller.teamRequest.dto.TeamRequestResponse;
import io.devarium.api.controller.user.dto.UpdateUserRequest;
import io.devarium.api.controller.user.dto.UserResponse;
import io.devarium.core.domain.membership.Membership;
import io.devarium.core.domain.team.Team;
import io.devarium.core.domain.teamRequest.TeamRequest;
import io.devarium.core.domain.teamRequest.TeamRequestStatus;
import io.devarium.core.domain.teamRequest.TeamRequestType;
import io.devarium.core.domain.user.User;
import io.devarium.core.domain.user.port.in.UserService;
import io.devarium.core.storage.Image;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/api/${api.version}/users")
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<SingleItemResponse<UserResponse>> getMe(
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        User user = userService.getUser(principal.getId());
        UserResponse response = UserResponse.from(user);

        return ResponseEntity.ok(SingleItemResponse.from(response));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<SingleItemResponse<UserResponse>> getUser(
        @PathVariable Long userId
    ) {
        User user = userService.getUser(userId);
        UserResponse response = UserResponse.from(user);

        return ResponseEntity.ok(SingleItemResponse.from(response));
    }

    @PutMapping("/me")
    public ResponseEntity<SingleItemResponse<UserResponse>> updateProfile(
        @Valid @RequestBody UpdateUserRequest request,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        User user = userService.updateProfile(request, principal.getUser());
        UserResponse response = UserResponse.from(user);

        return ResponseEntity.ok(SingleItemResponse.from(response));
    }

    @PutMapping("/me/profile-image")
    public ResponseEntity<SingleItemResponse<UserResponse>> updateProfileImage(
        @RequestPart MultipartFile file,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        User user = userService.updateProfileImage(Image.from(file), principal.getUser());
        UserResponse response = UserResponse.from(user);

        return ResponseEntity.ok(SingleItemResponse.from(response));
    }

    @GetMapping("/me/teams")
    public ResponseEntity<ListResponse<TeamResponse>> getTeams(
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        List<Team> teams = userService.getTeams(principal.getUser());
        List<TeamResponse> response = teams.stream()
            .map(TeamResponse::from).toList();
        return ResponseEntity.ok(ListResponse.from(response));
    }

    @GetMapping("/me/memberships")
    public ResponseEntity<ListResponse<MembershipResponse>> getMemberships(
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        List<Membership> memberships = userService.getMemberships(principal.getUser());
        List<MembershipResponse> response = memberships.stream()
            .map(MembershipResponse::from).toList();
        return ResponseEntity.ok(ListResponse.from(response));
    }

    @GetMapping("/me/join-requests")
    public ResponseEntity<ListResponse<TeamRequestResponse>> getJoinRequests(
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        List<TeamRequest> teamRequests = userService.getTeamRequests(
            TeamRequestType.JOIN_REQUEST,
            List.of(
                TeamRequestStatus.PENDING,
                TeamRequestStatus.ACCEPTED,
                TeamRequestStatus.REJECTED
            ),
            principal.getUser()
        );
        List<TeamRequestResponse> response = teamRequests.stream()
            .map(TeamRequestResponse::from).toList();
        return ResponseEntity.ok(ListResponse.from(response));
    }

    @GetMapping("/me/invitations")
    public ResponseEntity<ListResponse<TeamRequestResponse>> getInvitations(
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        List<TeamRequest> teamRequests = userService.getTeamRequests(
            TeamRequestType.INVITATION,
            List.of(TeamRequestStatus.PENDING),
            principal.getUser()
        );
        List<TeamRequestResponse> response = teamRequests.stream()
            .map(TeamRequestResponse::from).toList();
        return ResponseEntity.ok(ListResponse.from(response));
    }

    @PatchMapping("/me/invitations/{teamRequestId}")
    public ResponseEntity<SingleItemResponse<TeamRequestResponse>> updateInvitation(
        @PathVariable Long teamRequestId,
        @RequestParam TeamRequestStatus status,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        TeamRequest teamRequest = userService.updateInvitation(
            teamRequestId,
            status,
            principal.getUser()
        );
        TeamRequestResponse response = TeamRequestResponse.from(teamRequest);
        return ResponseEntity.ok(SingleItemResponse.from(response));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> withdraw(
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        userService.withdraw(principal.getUser());
        return ResponseEntity.noContent().build();
    }
}
