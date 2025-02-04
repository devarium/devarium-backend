package io.devarium.api.controller.team.dto;

import io.devarium.core.domain.team.port.UpdateTeamInfo;

public record UpdateTeamInfoRequest(
    String description,
    String githubUrl
) implements UpdateTeamInfo {

}
