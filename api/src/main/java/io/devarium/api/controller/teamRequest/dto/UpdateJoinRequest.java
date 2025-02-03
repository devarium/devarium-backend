package io.devarium.api.controller.teamRequest.dto;

import io.devarium.core.domain.teamRequest.port.UpdateJoin;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record UpdateJoinRequest(
    @NotEmpty List<Long> teamRequestIds
) implements UpdateJoin {

}
