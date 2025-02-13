package io.devarium.api.controller.team.dto;

import io.devarium.core.domain.team.command.UpdateLeader;
import jakarta.validation.constraints.NotNull;

public record UpdateLeaderRequest(
    @NotNull Long leaderId
) implements UpdateLeader {

}
