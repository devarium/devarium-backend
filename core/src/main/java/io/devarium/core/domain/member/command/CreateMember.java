package io.devarium.core.domain.member.command;

import io.devarium.core.domain.member.MemberRole;

public interface CreateMember {

    Long userId();

    MemberRole role();
}
