package io.devarium.core.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {
    SUPER_ADMIN(11),
    ADMIN(10),
    VIEWER(1);

    private final int level;

    public Boolean hasAccess(MemberRole requiredRole) {
        return this.level >= requiredRole.getLevel();
    }
}
