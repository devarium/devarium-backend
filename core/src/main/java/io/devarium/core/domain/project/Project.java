package io.devarium.core.domain.project;

import io.devarium.core.domain.skill.Skill;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Project {

    private final Long id;
    private final Instant createdAt;
    private final Set<Skill> skills;
    private String name;
    private String description;

    @Builder
    public Project(Long id, String name, String description, Instant createdAt, Set<Skill> skills) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.skills = skills != null ? new HashSet<>(skills) : new HashSet<>();
    }

    public Set<Skill> getSkills() {
        return Collections.unmodifiableSet(skills);
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateSkills(Set<Skill> skills) {
        this.skills.clear();
        this.skills.addAll(skills);
    }
}
