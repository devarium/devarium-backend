package io.devarium.core.domain.membership;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {
    SUPER_ADMIN(11),
    ADMIN(10),
    VIEWER(1);

    private final int level;
}
