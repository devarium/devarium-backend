package io.devarium.core.domain.project.service;

import io.devarium.core.domain.project.Project;
import io.devarium.core.domain.project.port.UpsertProject;
import io.devarium.core.domain.user.User;

public interface ProjectService {

    Project createProject(UpsertProject request, User user);

    Project getProject(Long projectId);

    Project updateProject(Long projectId, UpsertProject request, User user);

    void deleteProject(Long projectId, User user);
}
