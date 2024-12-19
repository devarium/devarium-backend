package io.devarium.infrastructure.auth;

import io.devarium.core.auth.EmailPrincipal;
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
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }
}
