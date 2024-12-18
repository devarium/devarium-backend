package io.devarium.infrastructure.auth.service;

import io.devarium.core.domain.user.exception.UserErrorCode;
import io.devarium.core.domain.user.exception.UserException;
import io.devarium.infrastructure.auth.CustomUserDetails;
import io.devarium.infrastructure.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserJpaRepository userJpaRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return null;
    }

    public UserDetails loadUserByEmail(String email) throws UserException {
        return userJpaRepository.findByEmail(email)
            .map(CustomUserDetails::new)
            .orElseThrow(() -> new UserException(UserErrorCode.USER_EMAIL_NOT_FOUND));
    }
}
