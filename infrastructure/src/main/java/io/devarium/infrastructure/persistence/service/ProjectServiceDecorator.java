package io.devarium.infrastructure.persistence.service;

import io.devarium.core.domain.project.Project;
import io.devarium.core.domain.project.command.UpsertProjectCommand;
import io.devarium.core.domain.project.service.ProjectService;
import io.devarium.core.domain.project.service.ProjectServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class ProjectServiceDecorator implements ProjectService {

    private final ProjectServiceImpl projectService;

    @Override
    @Transactional
    public Project createProject(UpsertProjectCommand command) {
        return projectService.createProject(command);
    }

    @Override
    @Transactional(readOnly = true)
    public Project getProject(Long ProjectId) {
        return projectService.getProject(ProjectId);
    }

    @Override
    @Transactional
    public Project updateProject(Long ProjectId, UpsertProjectCommand command) {
        return projectService.updateProject(ProjectId, command);
    }

    @Override
    @Transactional
    public void deleteProject(Long ProjectId) {
        projectService.deleteProject(ProjectId);
    }
}
