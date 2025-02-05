package io.devarium.core.domain.project.service;

import io.devarium.core.domain.project.Project;
import io.devarium.core.domain.project.ProjectStatus;
import io.devarium.core.domain.project.exception.ProjectErrorCode;
import io.devarium.core.domain.project.exception.ProjectException;
import io.devarium.core.domain.project.port.UpsertProject;
import io.devarium.core.domain.project.repository.ProjectRepository;
import io.devarium.core.domain.user.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Override
    public Project createProject(UpsertProject request, User user) {
        Project project = Project.builder()
            .name(request.name())
            .description(request.description())
            .status(ProjectStatus.TODO)
            .teamId(request.teamId())
            .skills(request.skills())
            .build();
        return projectRepository.save(project);
    }

    @Override
    public Project getProject(Long projectId) {
        return projectRepository.findById(projectId)
            .orElseThrow(() -> new ProjectException(ProjectErrorCode.PROJECT_NOT_FOUND, projectId));
    }

    @Override
    public Project updateProject(Long projectId, UpsertProject request, User user) {
        Project project = getProject(projectId);

        // TODO: 프로젝트 접근 권환 확인 필요

        project.updateName(request.name());
        project.updateDescription(request.description());
        project.updateSkills(request.skills());
        return projectRepository.save(project);
    }

    @Override
    public void deleteProject(Long projectId, User user) {
        // TODO: 프로젝트 접근 권환 확인 필요
        projectRepository.deleteById(projectId);
    }
}
