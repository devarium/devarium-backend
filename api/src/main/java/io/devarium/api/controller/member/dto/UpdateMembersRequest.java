package io.devarium.api.controller.member.dto;

import io.devarium.core.domain.member.MemberRole;
import io.devarium.core.domain.member.port.UpdateMember;
import io.devarium.core.domain.member.port.UpdateMembers;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public record UpdateMembersRequest(
    @NotEmpty(message = "'members' must not be empty") Set<UpdateMemberRequest> members
) implements UpdateMembers {

    public record UpdateMemberRequest(
        @NotNull(message = "'memberId' must not be null") Long memberId,

        @NotBlank(message = "'role' must not be blank") MemberRole role
    ) implements UpdateMember {

    }
}
