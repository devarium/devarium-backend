package io.devarium.api.controller.member.dto;

import io.devarium.core.domain.member.port.DeleteMembers;
import java.util.Set;

public record DeleteMembersRequest(
    Set<Long> memberIds
) implements DeleteMembers {

}
