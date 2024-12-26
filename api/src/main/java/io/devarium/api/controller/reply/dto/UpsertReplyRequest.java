package io.devarium.api.controller.reply.dto;

import io.devarium.core.domain.reply.port.UpsertReply;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpsertReplyRequest(
    @NotBlank(message = "'content' must not be blank") String content,
    @NotNull(message = "'commentId' must not be null") Long commentId
) implements UpsertReply {

}
