package io.devarium.api.controller.member;

import io.devarium.api.common.PageTypeConverter;
import io.devarium.api.common.dto.PagedListResponse;
import io.devarium.api.controller.member.dto.CreateMembersRequest;
import io.devarium.api.controller.member.dto.DeleteMembersRequest;
import io.devarium.api.controller.member.dto.MemberResponse;
import io.devarium.api.controller.member.dto.UpdateMembersRequest;
import io.devarium.core.auth.EmailPrincipal;
import io.devarium.core.domain.member.Member;
import io.devarium.core.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PostMapping("/{teamId}/members")
    public ResponseEntity<Void> createMembers(
        @PathVariable Long teamId,
        @Valid @RequestBody CreateMembersRequest request,
        @AuthenticationPrincipal EmailPrincipal emailPrincipal
    ) {
        memberService.createMembers(teamId, request, emailPrincipal.getUser());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{teamId}/members")
    public ResponseEntity<PagedListResponse<MemberResponse>> getMembersByTeam(
        @PageableDefault(size = Member.DEFAULT_PAGE_SIZE, sort = "createdAt", direction = Direction.ASC) Pageable pageable,
        @PathVariable Long teamId,
        @AuthenticationPrincipal EmailPrincipal emailPrincipal
    ) {
        Page<Member> members = memberService.getMembersByTeamId(
            pageable,
            teamId,
            emailPrincipal.getUser()
        );
        Page<MemberResponse> response = PageTypeConverter.convert(
            members,
            MemberResponse::from
        );

        return ResponseEntity.ok(PagedListResponse.from(response));
    }

    @GetMapping("/members")
    public ResponseEntity<PagedListResponse<MemberResponse>> getMembersByUser(
        @PageableDefault(size = Member.DEFAULT_PAGE_SIZE, sort = "createdAt", direction = Direction.ASC) Pageable pageable,
        @AuthenticationPrincipal EmailPrincipal emailPrincipal
    ) {
        Page<Member> members = memberService.getMembersByUser(pageable, emailPrincipal.getUser());

        Page<MemberResponse> response = PageTypeConverter.convert(
            members,
            MemberResponse::from
        );

        return ResponseEntity.ok(PagedListResponse.from(response));
    }

    @PutMapping("/{teamId}/members")
    public ResponseEntity<Void> updateMembers(
        @PathVariable Long teamId,
        @Valid @RequestBody UpdateMembersRequest request,
        @AuthenticationPrincipal EmailPrincipal emailPrincipal
    ) {
        memberService.updateMembers(teamId, request, emailPrincipal.getUser());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{teamId}/members")
    public ResponseEntity<Void> deleteMembers(
        @PathVariable Long teamId,
        @Valid @RequestBody DeleteMembersRequest request,
        @AuthenticationPrincipal EmailPrincipal emailPrincipal
    ) {
        memberService.deleteMembers(teamId, request, emailPrincipal.getUser());
        return ResponseEntity.noContent().build();
    }
}
