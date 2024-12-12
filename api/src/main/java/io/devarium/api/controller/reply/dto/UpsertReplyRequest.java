package io.devarium.api.controller.reply.dto;

import io.devarium.core.domain.reply.command.UpsertReplyCommand;
import jakarta.validation.constraints.NotBlank;

public record UpsertReplyRequest(
    @NotBlank(message = "'content' must not be blank") String content
) implements UpsertReplyCommand {

}
