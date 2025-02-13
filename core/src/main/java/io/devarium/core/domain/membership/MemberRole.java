package io.devarium.core.domain.membership;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {
    LEADER(3),
    MANAGER(2),
    MEMBER(1);

    private final int level;
}
