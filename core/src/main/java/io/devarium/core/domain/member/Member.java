package io.devarium.core.domain.member;

import io.devarium.core.domain.member.exception.MemberErrorCode;
import io.devarium.core.domain.member.exception.MemberException;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Member {

    public static final int DEFAULT_PAGE_SIZE = 10;

    private final Long id;
    private final Long userId;
    private final Long teamId;
    private MemberRole role;

    @Getter(AccessLevel.NONE)
    private Boolean isLeader;

    @Builder
    public Member(Long id, Long userId, Long teamId, MemberRole role, Boolean isLeader) {
        this.id = id;
        this.userId = userId;
        this.teamId = teamId;
        this.role = role;
        this.isLeader = isLeader;
    }

    public void validateMembership(Long teamId) {
        if (!Objects.equals(this.teamId, teamId)) {
            throw new MemberException(MemberErrorCode.MEMBER_NOT_IN_TEAM, this.userId, teamId);
        }
    }

    public void validateRole(MemberRole requiredRole) {
        if (this.role.getLevel() < requiredRole.getLevel()) {
            throw new MemberException(
                MemberErrorCode.FORBIDDEN_ACCESS,
                this.id,
                this.userId,
                this.teamId
            );
        }
    }

    public void update(MemberRole role) {
        if (role == MemberRole.ADMIN && this.isLeader) {
            this.isLeader = false;
        } else if (role == MemberRole.SUPER_ADMIN) {
            this.isLeader = true;
        }
        this.role = role;
    }

    public Boolean isLeader() {
        return this.isLeader;
    }
}
