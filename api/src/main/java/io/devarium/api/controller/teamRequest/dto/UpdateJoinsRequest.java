package io.devarium.api.controller.teamRequest.dto;

import io.devarium.core.domain.teamRequest.port.UpdateJoins;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

public record UpdateJoinsRequest(
    @NotEmpty Set<Long> ids
) implements UpdateJoins {

}
