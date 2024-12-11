package io.devarium.infrastructure.persistence.repository;

import io.devarium.core.domain.project.Project;
import io.devarium.core.domain.project.repository.ProjectRepository;
import io.devarium.infrastructure.persistence.entity.ProjectEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ProjectRepositoryImpl implements ProjectRepository {

    private final ProjectJpaRepository projectJpaRepository;

    @Override
    public Project save(Project project) {
        ProjectEntity entity = convertToEntity(project);
        ProjectEntity savedEntity = projectJpaRepository.save(entity);
        return convertToDomain(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        projectJpaRepository.deleteById(id);
    }

    @Override
    public Optional<Project> findById(Long id) {
        return projectJpaRepository.findById(id).map(this::convertToDomain);
    }

    private Project convertToDomain(ProjectEntity entity) {
        return Project.builder()
            .id(entity.getId())
            .name(entity.getName())
            .description(entity.getDescription())
            .skills(entity.getSkills())
            .createdAt(entity.getCreatedAt())
            .build();
    }

    private ProjectEntity convertToEntity(Project domain) {
        if (domain.getId() != null) {
            ProjectEntity entity = projectJpaRepository.findById(domain.getId()).orElseThrow();
            entity.update(domain);
            return entity;
        }

        return ProjectEntity.builder()
            .name(domain.getName())
            .description(domain.getDescription())
            .skills(domain.getSkills())
            .build();
    }
}
