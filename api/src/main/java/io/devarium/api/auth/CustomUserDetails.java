package io.devarium.api.auth;

import io.devarium.core.domain.user.User;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

public record CustomUserDetails(User user) implements OAuth2User, UserDetails {

    @Override
    public Map<String, Object> getAttributes() {
        return user.toAttributes();
    }

    @Override
    public String getName() {
        return getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(user.getRole());
    }

    @Deprecated
    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 상태 로직
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠금 상태 로직
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 인증 정보 만료 상태 로직
    }

    @Override
    public boolean isEnabled() {
        return user.getDeletedAt() == null; // 삭제 여부로 활성화 상태 판단
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return user.getId();
    }

    public String getEmail() {
        return user.getEmail();
    }

}
