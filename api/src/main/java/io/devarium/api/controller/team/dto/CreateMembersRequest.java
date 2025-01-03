package io.devarium.api.controller.team.dto;

import io.devarium.core.domain.team.port.CreateMembers;
import java.util.Set;

public record CreateMembersRequest(
    Set<Long> memberIds
) implements CreateMembers {

}
