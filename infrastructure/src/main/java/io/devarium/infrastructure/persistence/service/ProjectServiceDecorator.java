package io.devarium.infrastructure.persistence.service;

import io.devarium.core.domain.project.Project;
import io.devarium.core.domain.project.command.UpsertProject;
import io.devarium.core.domain.project.service.ProjectService;
import io.devarium.core.domain.project.service.ProjectServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class ProjectServiceDecorator implements ProjectService {

    private final ProjectServiceImpl projectService;

    @Override
    @Transactional
    public Project createProject(UpsertProject request) {
        return projectService.createProject(request);
    }

    @Override
    @Transactional(readOnly = true)
    public Project getProject(Long projectId) {
        return projectService.getProject(projectId);
    }

    @Override
    @Transactional
    public Project updateProject(Long projectId, UpsertProject request) {
        return projectService.updateProject(projectId, request);
    }

    @Override
    @Transactional
    public void deleteProject(Long projectId) {
        projectService.deleteProject(projectId);
    }
}
