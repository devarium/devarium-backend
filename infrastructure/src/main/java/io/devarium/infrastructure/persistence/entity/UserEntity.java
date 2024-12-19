package io.devarium.infrastructure.persistence.entity;

import io.devarium.core.auth.OAuth2Provider;
import io.devarium.core.domain.user.User;
import io.devarium.core.domain.user.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    private String bio;

    @Column(nullable = false)
    private String picture;

    private String blogUrl;

    private String githubUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OAuth2Provider provider;

    private Instant deletedAt;

    @Builder
    private UserEntity(
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

    public void update(User domain) {
        this.name = domain.getName();
        this.bio = domain.getBio();
        this.picture = domain.getPicture();
        this.blogUrl = domain.getBlogUrl();
        this.githubUrl = domain.getGithubUrl();
        this.deletedAt = domain.getDeletedAt();
    }
}
