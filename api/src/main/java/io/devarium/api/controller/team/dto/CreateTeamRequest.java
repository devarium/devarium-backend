package io.devarium.api.controller.team.dto;

import io.devarium.core.domain.team.command.UpsertTeam;
import jakarta.validation.constraints.NotBlank;

public record CreateTeamRequest(
    @NotBlank(message = "'name' must not be blank") String name,
    String description,
    String picture,
    String githubUrl
) implements UpsertTeam {

}
