package io.devarium.api.controller.project.dto;

import io.devarium.core.domain.project.command.UpsertProjectCommand;
import io.devarium.core.domain.skill.Skill;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public record UpsertProjectRequest(
    @NotBlank String name,
    String description,
    @NotNull Set<Skill> skills
) implements UpsertProjectCommand {

}
