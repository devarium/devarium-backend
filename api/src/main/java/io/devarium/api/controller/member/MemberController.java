package io.devarium.api.controller.member;

import io.devarium.api.auth.CustomUserPrincipal;
import io.devarium.api.common.dto.PagedListResponse;
import io.devarium.api.controller.member.dto.CreateMembersRequest;
import io.devarium.api.controller.member.dto.DeleteMembersRequest;
import io.devarium.api.controller.member.dto.MemberResponse;
import io.devarium.api.controller.member.dto.UpdateMembersRequest;
import io.devarium.core.domain.member.Member;
import io.devarium.core.domain.member.port.in.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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
@RequestMapping("/api/${api.version}")
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/teams/{teamId}/members")
    public ResponseEntity<Void> createMembers(
        @PathVariable Long teamId,
        @Valid @RequestBody CreateMembersRequest request,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        memberService.createMembers(teamId, request, principal.getUser());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/teams/{teamId}/members")
    public ResponseEntity<PagedListResponse<MemberResponse>> getMembersByTeam(
        @PageableDefault(size = Member.DEFAULT_PAGE_SIZE, sort = "createdAt", direction = Direction.ASC) Pageable pageable,
        @PathVariable Long teamId,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        Page<Member> members = memberService.getMembersByTeamId(
            pageable,
            teamId,
            principal.getUser()
        );
        Page<MemberResponse> response = members.map(MemberResponse::from);

        return ResponseEntity.ok(PagedListResponse.from(response));
    }

    @GetMapping("/members")
    public ResponseEntity<PagedListResponse<MemberResponse>> getMembersByUser(
        @PageableDefault(size = Member.DEFAULT_PAGE_SIZE, sort = "createdAt", direction = Direction.ASC) Pageable pageable,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        Page<Member> members = memberService.getMembersByUser(pageable, principal.getUser());
        Page<MemberResponse> response = members.map(MemberResponse::from);

        return ResponseEntity.ok(PagedListResponse.from(response));
    }

    @PutMapping("/teams/{teamId}/members")
    public ResponseEntity<Void> updateMembers(
        @PathVariable Long teamId,
        @Valid @RequestBody UpdateMembersRequest request,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        memberService.updateMembers(teamId, request, principal.getUser());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/teams/{teamId}/members")
    public ResponseEntity<Void> deleteMembers(
        @PathVariable Long teamId,
        @Valid @RequestBody DeleteMembersRequest request,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        memberService.deleteMembers(teamId, request, principal.getUser());
        return ResponseEntity.noContent().build();
    }
}
