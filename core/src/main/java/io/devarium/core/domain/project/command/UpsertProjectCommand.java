package io.devarium.core.domain.project.command;

import io.devarium.core.domain.skill.Skill;
import java.util.Set;

public interface UpsertProjectCommand {

    public String name();

    public String description();

    public Set<Skill> skills();
}
