package io.devarium.api.controller.member.dto;

import io.devarium.core.domain.member.Member;
import io.devarium.core.domain.member.MemberRole;

public record MemberResponse(
    Long id,
    Long userId,
    Long teamId,
    MemberRole role
) {

    public static MemberResponse from(Member member) {
        return new MemberResponse(
            member.getId(),
            member.getUserId(),
            member.getTeamId(),
            member.getRole()
        );
    }
}
