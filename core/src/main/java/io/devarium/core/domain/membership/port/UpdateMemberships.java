package io.devarium.core.domain.membership.port;

import java.util.Set;

public interface UpdateMemberships {

    Set<? extends UpdateMembership> memberships();
}
