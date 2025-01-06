package io.devarium.api.auth;

import io.devarium.core.domain.user.User;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

public record CustomUserDetails(User user, Map<String, Object> attributes) implements UserDetails,
    OAuth2User {

    // 기본 생성자 (속성 없는 경우)
    public CustomUserDetails(User user) {
        this(user, Map.of());
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return user.getEmail(); // OAuth2 고유 식별자
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

    @Deprecated
    @Override
    public String getUsername() {
        return null;
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
}
