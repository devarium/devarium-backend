package io.devarium.api.controller.team.dto;

import io.devarium.core.domain.member.MemberRole;
import io.devarium.core.domain.member.port.UpdateMembers;
import io.devarium.core.domain.team.port.UpdateLeader;
import java.util.Collections;
import java.util.Map;

public record UpdateLeaderRequest(
    Long leaderId,
    Map<Long, MemberRole> memberIdToRole
) implements UpdateLeader, UpdateMembers {

    public UpdateLeaderRequest(Long leaderId) {
        this(leaderId, Collections.emptyMap());
    }
}
