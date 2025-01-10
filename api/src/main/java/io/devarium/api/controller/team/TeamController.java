package io.devarium.api.controller.team;

import io.devarium.api.common.dto.PagedListResponse;
import io.devarium.api.common.dto.SingleItemResponse;
import io.devarium.api.controller.team.dto.CreateTeamRequest;
import io.devarium.api.controller.team.dto.TeamResponse;
import io.devarium.api.controller.team.dto.UpdateLeaderRequest;
import io.devarium.api.controller.team.dto.UpdateTeamRequest;
import io.devarium.core.auth.EmailPrincipal;
import io.devarium.core.domain.team.Team;
import io.devarium.core.domain.team.service.TeamService;
import io.devarium.core.domain.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/${api.version}/teams")
@RestController
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<SingleItemResponse<TeamResponse>> createTeam(
        @Valid @RequestBody CreateTeamRequest request,
        @AuthenticationPrincipal EmailPrincipal emailPrincipal
    ) {
        User user = emailPrincipal.getUser();
        Team team = teamService.createTeam(request, user);
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
        @PageableDefault(size = Team.DEFAULT_PAGE_SIZE) Pageable pageable,
        @AuthenticationPrincipal EmailPrincipal emailPrincipal
    ) {
        Page<Team> teams = teamService.getTeams(pageable, emailPrincipal.getUser());
        Page<TeamResponse> response = teams.map(TeamResponse::from);

        return ResponseEntity.ok(PagedListResponse.from(response));
    }

    @PutMapping("/{teamId}")
    public ResponseEntity<SingleItemResponse<TeamResponse>> updateTeam(
        @PathVariable Long teamId,
        @Valid @RequestBody UpdateTeamRequest request,
        @AuthenticationPrincipal EmailPrincipal emailPrincipal
    ) {
        Team team = teamService.updateTeam(teamId, request, emailPrincipal.getUser());
        TeamResponse response = TeamResponse.from(team);

        return ResponseEntity.ok(SingleItemResponse.from(response));
    }

    @PutMapping("/{teamId}/leader")
    public ResponseEntity<SingleItemResponse<TeamResponse>> updateLeader(
        @PathVariable Long teamId,
        @Valid @RequestBody UpdateLeaderRequest request,
        @AuthenticationPrincipal EmailPrincipal emailPrincipal
    ) {
        Team team = teamService.updateLeader(teamId, request, emailPrincipal.getUser());
        TeamResponse response = TeamResponse.from(team);

        return ResponseEntity.ok(SingleItemResponse.from(response));
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<Void> deleteTeam(
        @PathVariable Long teamId,
        @AuthenticationPrincipal EmailPrincipal emailPrincipal
    ) {
        teamService.deleteTeam(teamId, emailPrincipal.getUser());
        return ResponseEntity.noContent().build();
    }
}
