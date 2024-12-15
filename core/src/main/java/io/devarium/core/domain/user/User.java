package io.devarium.core.domain.user;

import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Getter
public class User {

    private final Long id;
    private final String email;
    private  String name;
    private  String picture;
    private final UserRole role;
    private final String provider;
    private final Instant createdAt;
    private Instant deletedAt;
    private String blogUrl;
    private String githubUrl;
    private String content;

    @Builder
    public User(Long id, String email, String name, String picture, UserRole role, String provider, Instant createdAt,String blogUrl, String githubUrl, String content) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.role = role;
        this.provider = provider;
        this.createdAt = createdAt;
        this.blogUrl = blogUrl;
        this.githubUrl= githubUrl;
        this.content = content;
    }

    public void update(String name, String picture) {
        this.name = name;
        this.picture = picture;
    }

    public void update(String blogUrl, String githubUrl, String content) {
        this.blogUrl = blogUrl;
        this.githubUrl = githubUrl;
        this.content = content;
    }

    public void delete(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }
}
