package io.devarium.api.controller.team.dto;

import io.devarium.core.domain.team.port.UpdateTeamName;
import jakarta.validation.constraints.NotNull;

public record UpdateTeamNameRequest(
    @NotNull String name
) implements UpdateTeamName {

}
