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
    private String nickname;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private UserRole role;

   /* @Column(nullable = false)
    private String provider; // "GOOGLE"*/

    /*    @Column
    private Instant deletedAt;*/

    @Builder
    private UserEntity(
        Long id,
        String email,
        String nickname,
        UserRole role
        //String provider,
        //Instant deletedAt
    ) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.role = role;
        //this.provider = provider;
        //this.deletedAt = deletedAt;
    }

}
