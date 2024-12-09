package io.devarium.api.controller.post.dto;

import io.devarium.core.domain.post.command.UpsertPostCommand;

public record UpsertPostRequest(String title, String content) implements UpsertPostCommand {

}
