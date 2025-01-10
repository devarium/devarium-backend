package io.devarium.core.domain.member.port;

import io.devarium.core.domain.member.MemberRole;

public interface UpdateMember {

    Long memberId();

    MemberRole role();
}
