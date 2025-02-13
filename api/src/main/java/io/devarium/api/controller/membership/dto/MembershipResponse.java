package io.devarium.api.controller.membership.dto;

import io.devarium.core.domain.membership.MemberRole;
import io.devarium.core.domain.membership.Membership;

public record MembershipResponse(
    Long id,
    Long userId,
    Long teamId,
    MemberRole role
) {

    public static MembershipResponse from(Membership membership) {
        return new MembershipResponse(
            membership.getId(),
            membership.getUserId(),
            membership.getTeamId(),
            membership.getRole()
        );
    }
}
