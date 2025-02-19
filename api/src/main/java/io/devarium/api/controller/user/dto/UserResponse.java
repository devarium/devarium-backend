package io.devarium.api.controller.user.dto;

import io.devarium.core.domain.user.User;
import java.time.Instant;

public record UserResponse(
    Long id,
    String email,
    String username,
    String bio,
    String profileImageUrl,
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
            user.getUsername(),
            user.getBio(),
            user.getProfileImageUrl(),
            user.getBlogUrl(),
            user.getGithubUrl(),
            user.getRole().getAuthority(),
            user.getProvider().toString(),
            user.getDeletedAt()
        );
    }
}
