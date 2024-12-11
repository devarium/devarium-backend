package io.devarium.api.controller.comment.dto;

import io.devarium.core.domain.comment.command.UpsertCommentCommand;
import jakarta.validation.constraints.NotBlank;

public record UpsertCommentRequest(
    @NotBlank(message = "'content' must not be blank.") String content
) implements UpsertCommentCommand {

}