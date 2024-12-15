package io.devarium.infrastructure.persistence.entity;

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
@Table(name="users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String picture;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(nullable = false)
    private String provider;

    @Column
    private String blogUrl;

    @Column
    private String githubUrl;

    @Column
    private String content;

    @Column
    private Instant deletedAt;

    @Builder
    private UserEntity(
        Long id,
        String email,
        String name,
        String picture,
        UserRole role,
        String provider,
        String blogUrl,
        String githubUrl,
        String content
    ) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.role = role;
        this.provider = provider;
        this.blogUrl = blogUrl;
        this.githubUrl = githubUrl;
        this.content = content;
    }
}
