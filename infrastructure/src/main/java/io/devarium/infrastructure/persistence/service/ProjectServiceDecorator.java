package io.devarium.infrastructure.persistence.service;

import io.devarium.core.domain.project.Project;
import io.devarium.core.domain.project.port.UpsertProject;
import io.devarium.core.domain.project.service.ProjectService;
import io.devarium.core.domain.project.service.ProjectServiceImpl;
import io.devarium.core.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class ProjectServiceDecorator implements ProjectService {

    private final ProjectServiceImpl projectService;

    @Override
    @Transactional
    public Project createProject(UpsertProject request, User user) {
        return projectService.createProject(request, user);
    }

    @Override
    @Transactional(readOnly = true)
    public Project getProject(Long projectId, User user) {
        return projectService.getProject(projectId, user);
    }

    @Override
    @Transactional
    public Project updateProject(Long projectId, UpsertProject request, User user) {
        return projectService.updateProject(projectId, request, user);
    }

    @Override
    @Transactional
    public void deleteProject(Long projectId, User user) {
        projectService.deleteProject(projectId, user);
    }
}
