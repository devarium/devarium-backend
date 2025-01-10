package io.devarium.api.controller.project;

import io.devarium.api.auth.CustomUserDetails;
import io.devarium.api.common.dto.SingleItemResponse;
import io.devarium.api.controller.project.dto.ProjectResponse;
import io.devarium.api.controller.project.dto.UpsertProjectRequest;
import io.devarium.core.domain.project.Project;
import io.devarium.core.domain.project.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/projects")
@RestController
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<SingleItemResponse<ProjectResponse>> createProject(
        @Valid @RequestBody UpsertProjectRequest request,
        @AuthenticationPrincipal CustomUserDetails principal
    ) {
        Project project = projectService.createProject(request, principal.getUser());
        ProjectResponse response = ProjectResponse.from(project);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(SingleItemResponse.from(response));
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<SingleItemResponse<ProjectResponse>> getProject(
        @PathVariable Long projectId
    ) {
        Project project = projectService.getProject(projectId);
        ProjectResponse response = ProjectResponse.from(project);

        return ResponseEntity.ok(SingleItemResponse.from(response));
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<SingleItemResponse<ProjectResponse>> updateProject(
        @PathVariable Long projectId,
        @Valid @RequestBody UpsertProjectRequest request,
        @AuthenticationPrincipal CustomUserDetails principal
    ) {
        Project project = projectService.updateProject(projectId, request, principal.getUser());
        ProjectResponse response = ProjectResponse.from(project);

        return ResponseEntity.ok(SingleItemResponse.from(response));
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(
        @PathVariable Long projectId,
        @AuthenticationPrincipal CustomUserDetails principal
    ) {
        projectService.deleteProject(projectId, principal.getUser());

        return ResponseEntity.noContent().build();
    }
}
