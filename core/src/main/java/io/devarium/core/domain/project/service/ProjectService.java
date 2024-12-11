package io.devarium.core.domain.project.service;

import io.devarium.core.domain.project.Project;
import io.devarium.core.domain.project.command.UpsertProjectCommand;

public interface ProjectService {

    Project createProject(UpsertProjectCommand command);

    Project getProject(Long projectId);

    Project updateProject(Long projectId, UpsertProjectCommand command);

    void deleteProject(Long projectId);
}
