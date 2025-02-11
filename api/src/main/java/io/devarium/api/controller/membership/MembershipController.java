package io.devarium.api.controller.membership;

import io.devarium.api.auth.CustomUserPrincipal;
import io.devarium.api.common.dto.ListResponse;
import io.devarium.api.common.dto.PagedListResponse;
import io.devarium.api.controller.membership.dto.DeleteMembershipsRequest;
import io.devarium.api.controller.membership.dto.MembershipResponse;
import io.devarium.api.controller.membership.dto.UpdateMembershipsRequest;
import io.devarium.core.domain.membership.Membership;
import io.devarium.core.domain.membership.service.MembershipService;
import jakarta.validation.Valid;
import java.util.List;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/${api.version}/teams/{teamId}/memberships")
@RestController
public class MembershipController {

    private final MembershipService membershipService;

    @GetMapping
    public ResponseEntity<PagedListResponse<MembershipResponse>> getMemberships(
        @PageableDefault(size = Membership.DEFAULT_PAGE_SIZE, sort = "createdAt", direction = Direction.ASC) Pageable pageable,
        @PathVariable Long teamId,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        Page<Membership> memberships = membershipService.getMemberships(
            pageable,
            teamId,
            principal.getUser()
        );
        Page<MembershipResponse> response = memberships.map(MembershipResponse::from);

        return ResponseEntity.ok(PagedListResponse.from(response));
    }

    @PutMapping
    public ResponseEntity<ListResponse<MembershipResponse>> updateMemberships(
        @PathVariable Long teamId,
        @Valid @RequestBody UpdateMembershipsRequest request,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        List<Membership> memberships = membershipService.updateMemberships(
            teamId,
            request,
            principal.getUser()
        );
        List<MembershipResponse> response = memberships.stream()
            .map(MembershipResponse::from).toList();

        return ResponseEntity.ok(ListResponse.from(response));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMemberships(
        @PathVariable Long teamId,
        @Valid @RequestBody DeleteMembershipsRequest request,
        @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        membershipService.deleteMemberships(teamId, request, principal.getUser());
        return ResponseEntity.noContent().build();
    }
}
