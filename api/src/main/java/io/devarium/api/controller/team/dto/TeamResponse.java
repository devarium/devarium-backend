package io.devarium.api.controller.team.dto;

import io.devarium.core.domain.team.Team;

public record TeamResponse(
    Long id,
    String name,
    String description,
    String picture,
    String githubUrl,
    Long leaderId
) {

    public static TeamResponse from(Team team) {
        return new TeamResponse(
            team.getId(),
            team.getName(),
            team.getDescription(),
            team.getPicture(),
            team.getGithubUrl(),
            team.getLeaderId()
        );
    }
}
