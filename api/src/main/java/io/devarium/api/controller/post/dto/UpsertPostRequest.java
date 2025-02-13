package io.devarium.api.controller.post.dto;

import io.devarium.core.domain.post.command.UpsertPost;
import jakarta.validation.constraints.NotBlank;

public record UpsertPostRequest(
    String title,
    @NotBlank(message = "'content' must not be blank") String content
) implements UpsertPost {

}
