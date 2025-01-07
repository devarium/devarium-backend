package io.devarium.core.domain.member.port;

import io.devarium.core.domain.member.MemberRole;
import java.util.Map;

public interface CreateMembers {

    Map<Long, MemberRole> userIdToRole();
}
