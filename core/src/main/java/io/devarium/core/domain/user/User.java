package io.devarium.core.domain.user;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public class User {

    private final Long id;
    private final String email;
    private final UserRole role;
    private final OAuth2Provider provider;
    private final Instant createdAt;
    private String name;
    private String picture;
    private Instant deletedAt;
    private String blogUrl;
    private String githubUrl;
    private String content;

    @Builder
    public User(Long id, String email, String name, String picture, UserRole role,
        OAuth2Provider provider, Instant createdAt, String blogUrl, String githubUrl,
        String content) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.role = role;
        this.provider = provider;
        this.createdAt = createdAt;
        this.blogUrl = blogUrl;
        this.githubUrl = githubUrl;
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

    public void delete() {
        this.deletedAt = Instant.now();
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(role); // UserRole은 이제 GrantedAuthority 구현체
    }

}
