package io.devarium.api.controller.user.dto;

import io.devarium.core.domain.user.User;
import java.time.Instant;

public record UserResponse(Long id, String email, String name, String picture, String role,
                           String provider, Instant createdAt, Instant deletedAt, String blogUrl,
                           String githubUrl, String content) {

    public static UserResponse from(User user) {
        return new UserResponse(
            user.getId(),
            user.getEmail(),
            user.getName(),
            user.getPicture(),
            user.getRole().getAuthority(),
            user.getProvider().toString(),
            user.getCreatedAt(),
            user.getDeletedAt(),
            user.getBlogUrl(),
            user.getGithubUrl(),
            user.getContent()
        );
    }
}
