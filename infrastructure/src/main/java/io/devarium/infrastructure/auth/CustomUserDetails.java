package io.devarium.infrastructure.auth;

import io.devarium.core.auth.EmailPrincipal;
import io.devarium.core.domain.user.User;
import io.devarium.infrastructure.persistence.entity.UserEntity;
import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public record CustomUserDetails(UserEntity userEntity) implements EmailPrincipal, UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(userEntity.getRole());
    }

    @Override
    public String getEmail() {
        return userEntity.getEmail();
    }

    @Override
    public Long getId() {
        return userEntity.getId();
    }

    @Override
    public User getUser() {
        return userEntity.toDomain();
    }

    @Deprecated
    @Override
    public String getPassword() {
        return null;
    }

    @Deprecated
    @Override
    public String getUsername() {
        return null;
    }
}
