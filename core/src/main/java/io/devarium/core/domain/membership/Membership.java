package io.devarium.core.domain.membership;

import io.devarium.core.domain.membership.exception.MembershipErrorCode;
import io.devarium.core.domain.membership.exception.MembershipException;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Membership {

    public static final int DEFAULT_PAGE_SIZE = 10;

    private final Long id;
    private final Long userId;
    private final Long teamId;
    private MemberRole role;

    @Builder
    public Membership(Long id, Long userId, Long teamId, MemberRole role) {
        this.id = id;
        this.userId = userId;
        this.teamId = teamId;
        this.role = role;
    }

    public void validateRole(MemberRole requiredRole) {
        if (this.role.getLevel() < requiredRole.getLevel()) {
            throw new MembershipException(
                MembershipErrorCode.FORBIDDEN_ACCESS,
                this.id,
                this.userId,
                this.teamId
            );
        }
    }

    public void update(MemberRole role) {
        this.role = role;
    }
}
