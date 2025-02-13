package io.devarium.api.controller.member.dto;

import io.devarium.core.domain.member.MemberRole;
import io.devarium.core.domain.member.command.CreateMember;
import io.devarium.core.domain.member.command.CreateMembers;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public record CreateMembersRequest(
    @NotEmpty(message = "'members' must not be empty") Set<CreateMemberRequest> members
) implements CreateMembers {

    public record CreateMemberRequest(
        @NotNull(message = "'userId' must not be null") Long userId,
        @NotBlank(message = "'role' must not be blank") MemberRole role
    ) implements CreateMember {

    }
}
