package io.devarium.core.domain.user;

import io.devarium.core.auth.OAuth2Provider;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public class User {

    private final Long id;
    private final String email;
    private final UserRole role;
    private final OAuth2Provider provider;

    private String username;
    private String profileImageUrl;
    private String blogUrl;
    private String githubUrl;
    private String bio;
    private Instant deletedAt;

    @Builder
    public User(
        Long id,
        String email,
        String username,
        String bio,
        String profileImageUrl,
        String blogUrl,
        String githubUrl,
        UserRole role,
        OAuth2Provider provider
    ) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.bio = bio;
        this.profileImageUrl = profileImageUrl;
        this.blogUrl = blogUrl;
        this.githubUrl = githubUrl;
        this.role = role;
        this.provider = provider;
    }

    public void update(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void update(String username, String bio, String blogUrl, String githubUrl) {
        this.username = username;
        this.bio = bio;
        this.blogUrl = blogUrl;
        this.githubUrl = githubUrl;
    }

    public void delete() {
        this.deletedAt = Instant.now();
    }

    // 소프트 딜리트 상태 확인 메서드
    public boolean isDeleted() {
        return deletedAt != null;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(role); // UserRole은 이제 GrantedAuthority 구현체
    }

    // OAuth2User의 구현체인 CustomUserDetails에서 메서드를 오버라이딩할때 필요
    public Map<String, Object> toAttributes() {
        return Map.of(
            "email", email,
            "name", username,
            "picture", profileImageUrl,
            "provider", provider
        );
    }
}
