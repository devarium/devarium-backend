package io.devarium.api.controller.membership.dto;

import io.devarium.core.domain.membership.command.DeleteMemberships;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

public record DeleteMembershipsRequest(
    @NotEmpty(message = "'ids' must not be empty") Set<Long> ids
) implements DeleteMemberships {

}
