package io.devarium.core.domain.project.command;

import io.devarium.core.domain.skill.Skill;
import java.util.Set;

public interface UpsertProject {

    public String name();

    public String description();

    public Long teamId();

    public Set<Skill> skills();
}
