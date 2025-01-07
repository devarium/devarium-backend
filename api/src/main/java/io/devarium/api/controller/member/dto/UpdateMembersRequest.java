package io.devarium.api.controller.member.dto;

import io.devarium.core.domain.member.MemberRole;
import io.devarium.core.domain.member.port.UpdateMembers;
import java.util.Map;

public record UpdateMembersRequest(
    Map<Long, MemberRole> memberIdToRole
) implements UpdateMembers {

}
