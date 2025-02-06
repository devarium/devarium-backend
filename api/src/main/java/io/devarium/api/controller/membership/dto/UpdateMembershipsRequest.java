package io.devarium.api.controller.membership.dto;

import io.devarium.core.domain.membership.MemberRole;
import io.devarium.core.domain.membership.port.UpdateMembership;
import io.devarium.core.domain.membership.port.UpdateMemberships;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public record UpdateMembershipsRequest(
    @NotEmpty(message = "'memberships' must not be empty") Set<UpdateMembershipRequest> memberships
) implements UpdateMemberships {

    public record UpdateMembershipRequest(
        @NotNull(message = "'membershipId' must not be null") Long membershipId,
        @NotBlank(message = "'role' must not be blank") MemberRole role
    ) implements UpdateMembership {

    }
}
