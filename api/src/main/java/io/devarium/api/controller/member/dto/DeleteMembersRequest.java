package io.devarium.api.controller.member.dto;

import io.devarium.core.domain.member.port.DeleteMembers;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

public record DeleteMembersRequest(
    @NotEmpty Set<Long> memberIds
) implements DeleteMembers {

}
