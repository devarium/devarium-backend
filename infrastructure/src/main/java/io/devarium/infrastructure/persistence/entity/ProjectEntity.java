package io.devarium.infrastructure.persistence.entity;

import io.devarium.core.domain.project.Project;
import io.devarium.core.domain.project.ProjectStatus;
import io.devarium.core.domain.skill.Skill;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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

    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_id", nullable = false, unique = true)
    private TeamEntity team;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(
        name = "project_skills",
        joinColumns = @JoinColumn(name = "project_id")
    )
    private Set<Skill> skills;

    @Builder
    private ProjectEntity(
        Long id,
        String name,
        String description,
        ProjectStatus status,
        Set<Skill> skills,
        TeamEntity team
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.skills = skills != null ? new HashSet<>(skills) : new HashSet<>();
        this.team = team;
    }

    public static ProjectEntity fromDomain(Project project) {
        return ProjectEntity.builder()
            .name(project.getName())
            .description(project.getDescription())
            .status(project.getStatus())
            .skills(new HashSet<>(project.getSkills()))
            .build();
    }

    public Project toDomain() {
        return Project.builder()
            .id(id)
            .name(name)
            .description(description)
            .status(status)
            .skills(new HashSet<>(skills))
            .build();
    }

    public void update(Project project) {
        this.name = project.getName();
        this.description = project.getDescription();
        this.status = project.getStatus();
        this.skills.clear();
        this.skills.addAll(project.getSkills());
    }
}
