package io.devarium.api.controller.member.dto;

import io.devarium.core.domain.member.MemberRole;
import io.devarium.core.domain.member.port.CreateMembers;
import java.util.Map;

public record CreateMembersRequest(
    Map<Long, MemberRole> userIdToRole
) implements CreateMembers {

}
