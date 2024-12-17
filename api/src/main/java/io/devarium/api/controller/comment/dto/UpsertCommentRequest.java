package io.devarium.api.controller.comment.dto;

import io.devarium.core.domain.comment.port.UpsertComment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpsertCommentRequest(
    @NotBlank(message = "'content' must not be blank") String content,
    @NotNull(message = "'postId' must not be null") Long postId
) implements UpsertComment {

}
