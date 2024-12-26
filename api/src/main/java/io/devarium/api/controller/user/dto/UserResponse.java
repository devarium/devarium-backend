package io.devarium.api.controller.user.dto;

import io.devarium.core.domain.user.User;
import java.time.Instant;

public record UserResponse(
    Long id,
    String email,
    String name,
    String bio,
    String picture,
    String blogUrl,
    String githubUrl,
    String role,
    String provider,
    Instant deletedAt
) {

    public static UserResponse from(User user) {
        return new UserResponse(
            user.getId(),
            user.getEmail(),
            user.getName(),
            user.getBio(),
            user.getPicture(),
            user.getBlogUrl(),
            user.getGithubUrl(),
            user.getRole().getAuthority(),
            user.getProvider().toString(),
            user.getDeletedAt()
        );
    }
}
