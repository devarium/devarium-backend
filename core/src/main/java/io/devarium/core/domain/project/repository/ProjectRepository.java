package io.devarium.core.domain.project.repository;

import io.devarium.core.domain.project.Project;
import java.util.Optional;

public interface ProjectRepository {

    Project save(Project project);

    void deleteById(Long id);

    Optional<Project> findById(Long id);
}
