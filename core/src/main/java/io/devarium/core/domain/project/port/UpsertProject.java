package io.devarium.core.domain.project.port;

import io.devarium.core.domain.skill.Skill;
import java.util.Set;

public interface UpsertProject {

    public String name();

    public String description();

    public Set<Skill> skills();
}
