package io.devarium.api.controller.teamRequest.dto;

import io.devarium.core.domain.teamRequest.port.CreateInvitation;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record CreateInvitationRequest(
    @NotEmpty List<Long> userIds
) implements CreateInvitation {

}
