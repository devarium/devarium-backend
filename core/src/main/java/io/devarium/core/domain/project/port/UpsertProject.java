package io.devarium.core.domain.project.port;

import io.devarium.core.domain.skill.Skill;
import java.util.Set;

public interface UpsertProject {

    String name();

    String description();

    Long teamId();

    Set<Skill> skills();
}
