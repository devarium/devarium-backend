package io.devarium.infrastructure.persistence.entity;

import io.devarium.core.domain.project.Project;
import io.devarium.core.domain.skill.Skill;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "projects")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    // TODO: 1:1 mapping to Team

    private String description;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(
        name = "project_skills",
        joinColumns = @JoinColumn(name = "project_id")
    )
    private Set<Skill> skills;

    @Builder
    private ProjectEntity(Long id, String name, String description, Set<Skill> skills) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.skills = skills != null ? new HashSet<>(skills) : new HashSet<>();
    }

    public void update(Project project) {
        this.name = project.getName();
        this.description = project.getDescription();
        this.skills.clear();
        this.skills.addAll(project.getSkills());
    }
}