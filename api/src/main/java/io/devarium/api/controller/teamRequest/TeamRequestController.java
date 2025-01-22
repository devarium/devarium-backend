package io.devarium.api.controller.teamRequest;

import io.devarium.api.auth.CustomUserPrincipal;
import io.devarium.api.common.dto.ListResponse;
import io.devarium.api.common.dto.SingleItemResponse;
import io.devarium.api.controller.teamRequest.dto.TeamRequestResponse;
import io.devarium.api.controller.teamRequest.dto.UpsertJoinRequest;
import io.devarium.core.domain.teamRequest.TeamRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<ListResponse<TeamRequestResponse>> createInvitation(
        @PathVariable Long teamId,
        @Valid @RequestBody UpsertJoinRequest request,
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

    @PatchMapping("/join-requests/{requestId}")
    public ResponseEntity<ListResponse<TeamRequestResponse>> updateJoinRequest(
        @PathVariable Long teamId,
        @PathVariable Long requestId,
        @RequestParam @Valid @NotNull @Pattern(regexp = "PENDING|ACCEPTED|REJECTED") String status,
        @RequestBody @Valid UpsertJoinRequest request,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        List<TeamRequest> teamRequests = teamRequestService.update(
            teamId,
            requestId,
            status,
            request,
            principal.getUser()
        );
        List<TeamRequestResponse> response = teamRequests.stream()
            .map(TeamRequestResponse::from).toList();

        return ResponseEntity.ok(ListResponse.from(response));
    }
}
