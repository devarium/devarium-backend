package io.devarium.api.controller.team.dto;

import io.devarium.core.domain.team.Team;
import java.time.Instant;
import java.util.Set;

public record TeamResponse(
    Long id,
    String name,
    String description,
    String picture,
    String githubUrl,
    Long leaderId,
    Set<Long> memberIds,
    Instant deletedAt
) {

    public static TeamResponse from(Team team) {
        return new TeamResponse(
            team.getId(),
            team.getName(),
            team.getDescription(),
            team.getPicture(),
            team.getGithubUrl(),
            team.getLeaderId(),
            team.getMemberIds(),
            team.getDeletedAt()
        );
    }
}
