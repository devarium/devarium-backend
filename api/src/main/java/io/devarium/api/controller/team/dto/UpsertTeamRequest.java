package io.devarium.api.controller.team.dto;

import io.devarium.core.domain.team.port.UpsertTeam;

public record UpsertTeamRequest(
    String name,
    String description,
    String picture,
    String githubUrl
) implements UpsertTeam {

}
