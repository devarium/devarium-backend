package io.devarium.core.domain.user;

import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Getter
public class User {
    private final Long id;
    private final String email;
    private final UserRole role;
    //private final String provider;
    private String nickname;
    //private final Instant deletedAt;


    @Builder
    public User(Long id, String email, String nickname, UserRole role/*, String provider, Instant deletedAt*/) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.role = role;
        //this.provider = provider;
        //this.deletedAt = deletedAt;
    }

}