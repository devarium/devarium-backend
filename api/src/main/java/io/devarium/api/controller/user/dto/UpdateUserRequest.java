package io.devarium.api.controller.user.dto;

import io.devarium.core.domain.user.port.UpdateUser;

public record UpdateUserRequest(
    String username,
    String bio,
    String blogUrl,
    String githubUrl
) implements UpdateUser {

}
