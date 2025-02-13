package io.devarium.api.controller.project.dto;

import io.devarium.core.domain.project.command.UpsertProject;
import io.devarium.core.domain.skill.Skill;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public record UpsertProjectRequest(
    @NotBlank(message = "'name' must not be blank") String name,
    String description,
    @NotNull(message = "'teamId' must not be null") Long teamId,
    @NotNull(message = "'skills' must not be null") Set<Skill> skills
) implements UpsertProject {

}
