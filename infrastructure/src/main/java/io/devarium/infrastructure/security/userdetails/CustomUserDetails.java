package io.devarium.infrastructure.security.userdetails;

import io.devarium.core.auth.command.UserDetailsInterface;
import io.devarium.infrastructure.persistence.entity.UserEntity;
import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public record CustomUserDetails(UserEntity userEntity) implements UserDetailsInterface,
    UserDetails {

    /*    @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return Collections.singleton(new SimpleGrantedAuthority(userEntity.getRole().name()));
        }*/
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(userEntity.getRole()); // UserRole이 GrantedAuthority 구현체
    }

    @Override
    public String getUsername() { // username 대신 email을 반환
        return userEntity.getEmail();
    }

    @Override
    public String getPassword() {
        return null; // 소셜 로그인에서는 비밀번호가 필요 없음
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO: 계정 만료 검증 (e.g. 회원가입 후 이메일 인증)
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO: 계정 잠금 상태 (e.g. 관리자 수동 잠금, 로그인 실패 횟수 초과)
        return true;
    }


    @Override
    public boolean isEnabled() {
        return userEntity.getDeletedAt() == null;
    }
}

