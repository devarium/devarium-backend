package io.devarium.api.controller.comment.dto;

import io.devarium.core.domain.comment.command.UpsertCommentCommand;

public record UpsertCommentRequest(String content) implements UpsertCommentCommand {

}
