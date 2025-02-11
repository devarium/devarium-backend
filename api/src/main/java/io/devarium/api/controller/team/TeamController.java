package io.devarium.api.controller.team;

import io.devarium.api.auth.CustomUserPrincipal;
import io.devarium.api.common.dto.PagedListResponse;
import io.devarium.api.common.dto.SingleItemResponse;
import io.devarium.api.controller.team.dto.CreateTeamRequest;
import io.devarium.api.controller.team.dto.TeamResponse;
import io.devarium.api.controller.team.dto.UpdateLeaderRequest;
import io.devarium.api.controller.team.dto.UpdateTeamInfoRequest;
import io.devarium.api.controller.team.dto.UpdateTeamNameRequest;
import io.devarium.core.domain.team.Team;
import io.devarium.core.domain.team.service.TeamService;
import io.devarium.core.storage.Image;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/api/${api.version}/teams")
@RestController
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<SingleItemResponse<TeamResponse>> createTeam(
        @Valid @RequestBody CreateTeamRequest request,
        @RequestPart(required = false) MultipartFile file,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        Team team = teamService.createTeam(
            request,
            file == null ? null : Image.from(file),
            principal.getUser()
        );
        TeamResponse response = TeamResponse.from(team);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(SingleItemResponse.from(response));
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<SingleItemResponse<TeamResponse>> getTeam(@PathVariable Long teamId) {
        Team team = teamService.getTeam(teamId);
        TeamResponse response = TeamResponse.from(team);

        return ResponseEntity.ok(SingleItemResponse.from(response));
    }

    @GetMapping
    public ResponseEntity<PagedListResponse<TeamResponse>> getTeams(
        @PageableDefault(size = Team.DEFAULT_PAGE_SIZE, sort = "createdAt", direction = Direction.ASC) Pageable pageable,
        @RequestParam String name
    ) {
        Page<Team> teams = teamService.getTeams(pageable, name);
        Page<TeamResponse> response = teams.map(TeamResponse::from);

        return ResponseEntity.ok(PagedListResponse.from(response));
    }

    @PutMapping("/{teamId}/info")
    public ResponseEntity<SingleItemResponse<TeamResponse>> updateTeamInfo(
        @PathVariable Long teamId,
        @Valid @RequestBody UpdateTeamInfoRequest request,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        Team team = teamService.updateTeamInfo(teamId, request, principal.getUser());
        TeamResponse response = TeamResponse.from(team);

        return ResponseEntity.ok(SingleItemResponse.from(response));
    }

    @PutMapping("/{teamId}/name")
    public ResponseEntity<SingleItemResponse<TeamResponse>> updateTeamName(
        @PathVariable Long teamId,
        @Valid @RequestBody UpdateTeamNameRequest request,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        Team team = teamService.updateTeamName(teamId, request, principal.getUser());
        TeamResponse response = TeamResponse.from(team);

        return ResponseEntity.ok(SingleItemResponse.from(response));
    }

    @PutMapping("/{teamId}/profile-image")
    public ResponseEntity<SingleItemResponse<TeamResponse>> updateTeamProfileImage(
        @PathVariable Long teamId,
        @RequestPart MultipartFile file,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        Team team = teamService.updateTeamProfileImage(
            teamId,
            Image.from(file),
            principal.getUser()
        );
        TeamResponse response = TeamResponse.from(team);

        return ResponseEntity.ok(SingleItemResponse.from(response));
    }

    @PutMapping("/{teamId}/leader")
    public ResponseEntity<SingleItemResponse<TeamResponse>> updateLeader(
        @PathVariable Long teamId,
        @Valid @RequestBody UpdateLeaderRequest request,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        Team team = teamService.updateLeader(teamId, request, principal.getUser());
        TeamResponse response = TeamResponse.from(team);

        return ResponseEntity.ok(SingleItemResponse.from(response));
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<Void> deleteTeam(
        @PathVariable Long teamId,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        teamService.deleteTeam(teamId, principal.getUser());
        return ResponseEntity.noContent().build();
    }
}
