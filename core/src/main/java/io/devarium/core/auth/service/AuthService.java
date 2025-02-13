package io.devarium.core.auth.service;

import io.devarium.core.auth.Token;
import io.devarium.core.auth.port.in.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final TokenService tokenService;

    public Token refresh(String refreshToken) {
        return tokenService.refreshTokens(refreshToken);
    }

    public void logout(String email) {
        tokenService.deleteRefreshTokenByEmail(email);
        log.info("User logged out successfully: {}", email);
    }
}
