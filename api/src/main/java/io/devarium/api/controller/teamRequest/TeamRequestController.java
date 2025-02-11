package io.devarium.api.controller.teamRequest;

import io.devarium.api.auth.CustomUserPrincipal;
import io.devarium.api.common.dto.ListResponse;
import io.devarium.api.common.dto.SingleItemResponse;
import io.devarium.api.controller.teamRequest.dto.CreateInvitationsRequest;
import io.devarium.api.controller.teamRequest.dto.TeamRequestResponse;
import io.devarium.api.controller.teamRequest.dto.UpdateJoinsRequest;
import io.devarium.core.domain.teamRequest.TeamRequest;
import io.devarium.core.domain.teamRequest.TeamRequestStatus;
import io.devarium.core.domain.teamRequest.TeamRequestType;
import io.devarium.core.domain.teamRequest.service.TeamRequestService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RequestMapping("/api/${api.version}/teams/{teamId}")
public class TeamRequestController {

    private final TeamRequestService teamRequestService;

    @PostMapping("/join-requests")
    public ResponseEntity<SingleItemResponse<TeamRequestResponse>> createJoinRequest(
        @PathVariable Long teamId,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        TeamRequest teamRequest = teamRequestService.join(teamId, principal.getUser());
        TeamRequestResponse response = TeamRequestResponse.from(teamRequest);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(SingleItemResponse.from(response));
    }

    @PostMapping("/invitations")
    public ResponseEntity<ListResponse<TeamRequestResponse>> createInvitations(
        @PathVariable Long teamId,
        @Valid @RequestBody CreateInvitationsRequest request,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        List<TeamRequest> teamRequests = teamRequestService.invite(
            teamId,
            request,
            principal.getUser()
        );
        List<TeamRequestResponse> response = teamRequests.stream()
            .map(TeamRequestResponse::from).toList();

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ListResponse.from(response));
    }

    @GetMapping("/join-requests")
    public ResponseEntity<ListResponse<TeamRequestResponse>> getJoinRequests(
        @PathVariable Long teamId,
        @RequestParam List<TeamRequestStatus> status,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        List<TeamRequest> teamRequests = teamRequestService.getTeamRequests(
            teamId,
            TeamRequestType.JOIN_REQUEST,
            status,
            principal.getUser()
        );
        List<TeamRequestResponse> response = teamRequests.stream()
            .map(TeamRequestResponse::from).toList();
        return ResponseEntity.ok(ListResponse.from(response));
    }

    @GetMapping("/invitations")
    public ResponseEntity<ListResponse<TeamRequestResponse>> getInvitations(
        @PathVariable Long teamId,
        @RequestParam List<TeamRequestStatus> status,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        List<TeamRequest> teamRequests = teamRequestService.getTeamRequests(
            teamId,
            TeamRequestType.INVITATION,
            status,
            principal.getUser()
        );
        List<TeamRequestResponse> response = teamRequests.stream()
            .map(TeamRequestResponse::from).toList();
        return ResponseEntity.ok(ListResponse.from(response));
    }

    @PatchMapping("/join-requests")
    public ResponseEntity<ListResponse<TeamRequestResponse>> updateJoinRequests(
        @PathVariable Long teamId,
        @RequestParam TeamRequestStatus status,
        @RequestBody @Valid UpdateJoinsRequest request,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        List<TeamRequest> teamRequests = teamRequestService.updateJoinRequests(
            teamId,
            status,
            request,
            principal.getUser()
        );
        List<TeamRequestResponse> response = teamRequests.stream()
            .map(TeamRequestResponse::from).toList();

        return ResponseEntity.ok(ListResponse.from(response));
    }
}
