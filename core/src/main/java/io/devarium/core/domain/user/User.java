package io.devarium.core.domain.user;

import io.devarium.core.auth.OAuth2Provider;
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

    private String name;
    private String picture;
    private String blogUrl;
    private String githubUrl;
    private String bio;
    private Instant deletedAt;

    @Builder
    public User(
        Long id,
        String email,
        String name,
        String bio,
        String picture,
        String blogUrl,
        String githubUrl,
        UserRole role,
        OAuth2Provider provider
    ) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.bio = bio;
        this.picture = picture;
        this.blogUrl = blogUrl;
        this.githubUrl = githubUrl;
        this.role = role;
        this.provider = provider;
    }

    public void update(String name, String picture) {
        this.name = name;
        this.picture = picture;
    }

    public void update(String bio, String blogUrl, String githubUrl) {
        this.bio = bio;
        this.blogUrl = blogUrl;
        this.githubUrl = githubUrl;
    }

    public void delete() {
        this.deletedAt = Instant.now();
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(role); // UserRole은 이제 GrantedAuthority 구현체
    }
}
