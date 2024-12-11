package io.devarium.core.domain.project.service;

import io.devarium.core.domain.project.Project;
import io.devarium.core.domain.project.command.UpsertProjectCommand;
import io.devarium.core.domain.project.exception.ProjectErrorCode;
import io.devarium.core.domain.project.exception.ProjectException;
import io.devarium.core.domain.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Override
    public Project createProject(UpsertProjectCommand command) {
        Project project = Project.builder()
            .name(command.name())
            .description(command.description())
            .skills(command.skills())
            .build();
        return projectRepository.save(project);
    }

    @Override
    public Project getProject(Long projectId) {
        return projectRepository.findById(projectId)
            .orElseThrow(() -> new ProjectException(ProjectErrorCode.PROJECT_NOT_FOUND, projectId));
    }

    @Override
    public Project updateProject(Long projectId, UpsertProjectCommand command) {
        Project project = getProject(projectId);
        project.updateName(command.name());
        project.updateDescription(command.description());
        project.updateSkills(command.skills());
        return projectRepository.save(project);
    }

    @Override
    public void deleteProject(Long projectId) {
        projectRepository.deleteById(projectId);
    }
}
