package io.devarium.core.auth;

import io.devarium.core.domain.user.User;

public interface EmailPrincipal {

    String getEmail();

    Long getId();

    User getUser();
}
