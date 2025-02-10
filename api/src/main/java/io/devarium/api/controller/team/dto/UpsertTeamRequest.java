package io.devarium.api.controller.team.dto;

import io.devarium.core.domain.team.command.UpsertTeam;

public record UpsertTeamRequest(
    String name,
    String description,
    String picture,
    String githubUrl
) implements UpsertTeam {

}
