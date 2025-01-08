package io.devarium.api.controller.member.dto;

import io.devarium.core.domain.member.MemberRole;
import io.devarium.core.domain.member.port.UpdateMembers;
import jakarta.validation.constraints.NotEmpty;
import java.util.Map;

public record UpdateMembersRequest(
    @NotEmpty Map<Long, MemberRole> memberIdToRole
) implements UpdateMembers {

}
