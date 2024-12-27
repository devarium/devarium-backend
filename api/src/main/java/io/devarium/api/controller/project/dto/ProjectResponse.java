package io.devarium.api.controller.project.dto;

import io.devarium.core.domain.project.Project;
import io.devarium.core.domain.skill.Skill;
import java.time.Instant;
import java.util.Set;

public record ProjectResponse(
    Long projectId,
    String name,
    String description,
    Set<Skill> skills,
    Instant createdAt
) {

    public static ProjectResponse from(Project project) {
        return new ProjectResponse(
            project.getId(),
            project.getName(),
            project.getDescription(),
            project.getSkills(),
            project.getCreatedAt()
        );
    }
}
