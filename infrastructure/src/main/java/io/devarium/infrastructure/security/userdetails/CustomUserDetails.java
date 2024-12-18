package io.devarium.infrastructure.security.userdetails;

import io.devarium.infrastructure.persistence.entity.UserEntity;
import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public record CustomUserDetails(UserEntity userEntity) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(userEntity.getRole());
    }

    @Override
    public String getPassword() {
        return null;
    }
    
    @Override
    public String getUsername() {
        return null;
    }

    public String getEmail() {
        return userEntity.getEmail();
    }
}
