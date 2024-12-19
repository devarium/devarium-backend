package io.devarium.infrastructure.auth.service;

import io.devarium.core.auth.Token;
import io.devarium.core.auth.exception.AuthErrorCode;
import io.devarium.core.auth.exception.CustomAuthException;
import io.devarium.core.auth.exception.TokenStorageErrorCode;
import io.devarium.core.auth.exception.TokenStorageException;
import io.devarium.core.auth.repository.RefreshTokenRepository;
import io.devarium.core.auth.service.TokenService;
import io.devarium.infrastructure.auth.jwt.JwtUtil;
import java.util.Collection;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
class TokenServiceImpl implements TokenService {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public Token generateTokens(
        String email,
        Collection<? extends GrantedAuthority> authorities
    ) {
        String accessToken = jwtUtil.generateAccessToken(email, authorities);
        String refreshToken = jwtUtil.generateRefreshToken(email, authorities);

        refreshTokenRepository.save(email, refreshToken);

        return Token.of(accessToken, refreshToken);
    }

    @Override
    public Token refreshTokens(String refreshToken) {
        // 1. Refresh Token 유효성 검증
        if (!jwtUtil.isTokenValid(refreshToken)) {
            throw new CustomAuthException(AuthErrorCode.INVALID_TOKEN);
        }

        // 2. Refresh Token에서 사용자명 추출
        String username = jwtUtil.extractEmail(refreshToken);

        // 3. 저장된 Refresh Token 확인 및 클라이언트가 보낸 Refresh Token과 저장된 토큰 비교
        Optional<String> savedToken = refreshTokenRepository.findByEmail(username);
        if (savedToken.isEmpty()) {
            throw new TokenStorageException(TokenStorageErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }
        if (!savedToken.get().equals(refreshToken)) {
            throw new CustomAuthException(AuthErrorCode.INVALID_TOKEN);
        }

        // 4. 새 Access Token 및 Refresh Token 발급
        Collection<? extends GrantedAuthority> authorities = jwtUtil.extractAuthorities(
            refreshToken);
        String newAccessToken = jwtUtil.generateAccessToken(username, authorities);
        String newRefreshToken = jwtUtil.generateRefreshToken(username, authorities);

        // 5. 기존 Refresh Token 제거 및 새 Refresh Token 저장
        refreshTokenRepository.deleteByEmail(username);
        refreshTokenRepository.save(username, newRefreshToken);

        return Token.of(newAccessToken, newRefreshToken);
    }

    @Override
    public void deleteRefreshTokenByEmail(String email) {
        refreshTokenRepository.deleteByEmail(email);
    }
}
