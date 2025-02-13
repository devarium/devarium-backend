package io.devarium.core.domain.membership.command;

import java.util.Set;

public interface UpdateMemberships {

    Set<? extends UpdateMembership> memberships();
}
