package io.devarium.api.controller.project;

import io.devarium.api.common.dto.SingleItemResponse;
import io.devarium.api.controller.post.dto.PostResponse;
import io.devarium.api.controller.post.dto.UpsertPostRequest;
import io.devarium.api.controller.project.dto.ProjectResponse;
import io.devarium.api.controller.project.dto.UpsertProjectRequest;
import io.devarium.core.domain.project.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        @Valid @RequestBody UpsertProjectRequest request
    ) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(SingleItemResponse.from(null));
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<SingleItemResponse<ProjectResponse>> getProject(
        @PathVariable Long projectId
    ) {
        return ResponseEntity.ok(SingleItemResponse.from(null));
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<SingleItemResponse<PostResponse>> updateProject(
        @PathVariable Long projectId,
        @Valid @RequestBody UpsertPostRequest request
    ) {
        return ResponseEntity.ok(SingleItemResponse.from(null));
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        return ResponseEntity.noContent().build();
    }
}
