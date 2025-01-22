package io.devarium.api.controller.teamRequest.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record UpsertJoinRequest(
    @NotEmpty List<Long> userIds
) implements UpsertJoin {

}
