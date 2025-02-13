package io.devarium.api.controller.team.dto;

import io.devarium.core.domain.team.command.UpdateTeamInfo;

public record UpdateTeamInfoRequest(
    String description,
    String githubUrl
) implements UpdateTeamInfo {

}
