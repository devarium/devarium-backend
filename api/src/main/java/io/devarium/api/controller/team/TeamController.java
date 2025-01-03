package io.devarium.api.controller.team;

import io.devarium.api.common.dto.PagedListResponse;
import io.devarium.api.common.dto.SingleItemResponse;
import io.devarium.api.controller.team.dto.CreateMembersRequest;
import io.devarium.api.controller.team.dto.DeleteMembersRequest;
import io.devarium.api.controller.team.dto.TeamResponse;
import io.devarium.api.controller.team.dto.UpdateLeaderRequest;
import io.devarium.api.controller.team.dto.UpsertTeamRequest;
import io.devarium.core.auth.EmailPrincipal;
import io.devarium.core.domain.team.Team;
import io.devarium.core.domain.team.service.TeamService;
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
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/${api.version}/teams")
@RestController
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<SingleItemResponse<TeamResponse>> createTeam(
        @Valid @RequestBody UpsertTeamRequest request,
        @AuthenticationPrincipal EmailPrincipal emailPrincipal
    ) {
        Team team = teamService.createTeam(request, emailPrincipal.getUser());
        TeamResponse response = TeamResponse.from(team);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(SingleItemResponse.from(response));
    }

    // TODO : 사용자의 팀 가입 순서 페이지 정렬
    @GetMapping
    public ResponseEntity<PagedListResponse<Team>> getTeams(
        @PageableDefault(size = Team.DEFAULT_PAGE_SIZE, sort = "createdAt", direction = Direction.ASC) Pageable pageable,
        @AuthenticationPrincipal EmailPrincipal emailPrincipal
    ) {
        Page<Team> teams = teamService.getTeams(pageable, emailPrincipal.getUser());

        return ResponseEntity.ok(PagedListResponse.from(teams));
    }

    @PutMapping("/{teamId}")
    public ResponseEntity<SingleItemResponse<TeamResponse>> updateTeam(
        @PathVariable Long teamId,
        @Valid @RequestBody UpsertTeamRequest request,
        @AuthenticationPrincipal EmailPrincipal emailPrincipal
    ) {
        Team team = teamService.updateTeam(teamId, request, emailPrincipal.getUser());
        TeamResponse response = TeamResponse.from(team);

        return ResponseEntity.ok(SingleItemResponse.from(response));
    }

    // TODO : 팀 리더 1명이 아닌 팀 관리자 셋으로 변경
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

    @PostMapping("/{teamId}/members")
    public ResponseEntity<SingleItemResponse<TeamResponse>> createMembers(
        @PathVariable Long teamId,
        @Valid @RequestBody CreateMembersRequest request,
        @AuthenticationPrincipal EmailPrincipal emailPrincipal
    ) {
        Team team = teamService.createMembers(teamId, request, emailPrincipal.getUser());
        TeamResponse response = TeamResponse.from(team);

        return ResponseEntity.ok(SingleItemResponse.from(response));
    }

    @DeleteMapping("/{teamId}/members")
    public ResponseEntity<SingleItemResponse<TeamResponse>> deleteMembers(
        @PathVariable Long teamId,
        @Valid @RequestBody DeleteMembersRequest request,
        @AuthenticationPrincipal EmailPrincipal emailPrincipal
    ) {
        Team team = teamService.deleteMembers(teamId, request, emailPrincipal.getUser());
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
