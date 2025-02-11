package io.devarium.api.controller.teamRequest.dto;

import io.devarium.core.domain.teamRequest.port.CreateInvitations;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

public record CreateInvitationsRequest(
    @NotEmpty Set<Long> userIds
) implements CreateInvitations {

}
