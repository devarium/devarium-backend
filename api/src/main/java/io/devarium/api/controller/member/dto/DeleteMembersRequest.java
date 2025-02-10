package io.devarium.api.controller.member.dto;

import io.devarium.core.domain.member.command.DeleteMembers;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

public record DeleteMembersRequest(
    @NotEmpty(message = "'memberIds' must not be empty") Set<Long> memberIds
) implements DeleteMembers {

}
