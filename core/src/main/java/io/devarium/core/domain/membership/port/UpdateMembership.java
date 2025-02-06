package io.devarium.core.domain.membership.port;

import io.devarium.core.domain.membership.MemberRole;

public interface UpdateMembership {

    Long membershipId();

    MemberRole role();
}
