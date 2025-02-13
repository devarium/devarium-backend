package io.devarium.core.domain.project.command;

import io.devarium.core.domain.skill.Skill;
import java.util.Set;

public interface UpsertProject {

    String name();

    String description();

    Long teamId();

    Set<Skill> skills();
}
