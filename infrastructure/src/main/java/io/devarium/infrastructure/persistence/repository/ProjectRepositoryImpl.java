package io.devarium.infrastructure.persistence.repository;

import io.devarium.core.domain.project.Project;
import io.devarium.core.domain.project.exception.ProjectErrorCode;
import io.devarium.core.domain.project.exception.ProjectException;
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
        if (project.getId() != null) {
            ProjectEntity entity = projectJpaRepository.findById(project.getId())
                .orElseThrow(() -> new ProjectException(
                    ProjectErrorCode.PROJECT_NOT_FOUND,
                    project.getId())
                );
            entity.update(project);
            return projectJpaRepository.save(entity).toDomain();
        }

        ProjectEntity entity = ProjectEntity.fromDomain(project);
        return projectJpaRepository.save(entity).toDomain();
    }

    @Override
    public void deleteById(Long id) {
        projectJpaRepository.deleteById(id);
    }

    @Override
    public Optional<Project> findById(Long id) {
        return projectJpaRepository.findById(id).map(ProjectEntity::toDomain);
    }
}
