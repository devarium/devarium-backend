package io.devarium.api.controller.team.dto;

import io.devarium.core.domain.team.port.DeleteMembers;
import java.util.Set;

public record DeleteMembersRequest(
    Set<Long> memberIds
) implements DeleteMembers {

}
