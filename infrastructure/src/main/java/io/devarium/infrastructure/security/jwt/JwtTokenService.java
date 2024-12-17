package io.devarium.infrastructure.security.jwt;

import io.devarium.core.auth.Token;
import io.devarium.core.auth.repository.RefreshTokenRepository;
import io.devarium.core.auth.service.TokenService;
import io.devarium.infrastructure.security.jwt.util.JwtUtil;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenService implements TokenService {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtTokenService(JwtUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public Token generateTokens(String username, Collection<? extends GrantedAuthority> authorities) {
        String accessToken = jwtUtil.generateAccessToken(username, authorities);
        String refreshToken = jwtUtil.generateRefreshToken(username, authorities);

        refreshTokenRepository.save(username, refreshToken);

        return Token.of(accessToken, refreshToken);
    }
}
