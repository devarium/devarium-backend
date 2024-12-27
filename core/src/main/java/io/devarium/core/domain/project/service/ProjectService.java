package io.devarium.core.domain.project.service;

import io.devarium.core.domain.project.Project;
import io.devarium.core.domain.project.port.UpsertProject;

public interface ProjectService {

    Project createProject(UpsertProject request);

    Project getProject(Long projectId);

    Project updateProject(Long projectId, UpsertProject request);

    void deleteProject(Long projectId);
}
