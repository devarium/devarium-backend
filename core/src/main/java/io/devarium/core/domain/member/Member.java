package io.devarium.core.domain.member;

import io.devarium.core.domain.member.exception.MemberErrorCode;
import io.devarium.core.domain.member.exception.MemberException;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Member {

    public static final int DEFAULT_PAGE_SIZE = 10;

    private final Long id;
    private final Long userId;
    private final Long teamId;
    private MemberRole role;

    @Builder
    public Member(Long id, Long userId, Long teamId, MemberRole role) {
        this.id = id;
        this.userId = userId;
        this.teamId = teamId;
        this.role = role;
    }

    public void validateMember(Long teamId) {
        if (!Objects.equals(this.teamId, teamId)) {
            throw new MemberException(MemberErrorCode.MEMBER_NOT_IN_TEAM, this.userId, teamId);
        }
    }

    public void validateRole(MemberRole role) {
        if (!Objects.equals(this.role, role)) {
            throw new MemberException(
                MemberErrorCode.FORBIDDEN_ACCESS,
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
