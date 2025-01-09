package io.devarium.api.controller.member.dto;

import io.devarium.core.domain.member.MemberRole;
import io.devarium.core.domain.member.port.CreateMembers;
import jakarta.validation.constraints.NotEmpty;
import java.util.Map;

public record CreateMembersRequest(
    @NotEmpty Map<Long, MemberRole> userIdToRole
) implements CreateMembers {

}
