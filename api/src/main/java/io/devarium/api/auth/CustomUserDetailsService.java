package io.devarium.api.auth;

import io.devarium.core.domain.user.User;
import io.devarium.core.domain.user.port.in.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // UserService를 통해 사용자 정보를 조회
        User user = userService.getByEmail(email);

        // User 객체를 UserDetails로 변환하여 반환
        return new CustomUserPrincipal(user);
    }
}
