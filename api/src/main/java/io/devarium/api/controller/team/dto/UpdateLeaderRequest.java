package io.devarium.api.controller.team.dto;

import io.devarium.api.common.validator.EmptyCollection;
import io.devarium.core.domain.member.MemberRole;
import io.devarium.core.domain.member.port.UpdateMembers;
import io.devarium.core.domain.team.port.UpdateLeader;
import jakarta.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Map;

public record UpdateLeaderRequest(
    @NotNull Long leaderId,
    @EmptyCollection Map<Long, MemberRole> memberIdToRole
) implements UpdateLeader, UpdateMembers {

    public UpdateLeaderRequest(Long leaderId) {
        this(leaderId, Collections.emptyMap());
    }
}
