package io.devarium.core.domain.member.port;

import io.devarium.core.domain.member.MemberRole;
import java.util.Set;

public interface CreateMembers {

    Set<CreateMember> members();

    interface CreateMember {

        Long userId();

        MemberRole role();
    }
}
