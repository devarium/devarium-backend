package io.devarium.infrastructure.redis.repository;

import io.devarium.core.auth.port.out.RefreshTokenRepository;
import io.devarium.infrastructure.auth.jwt.JwtProperties;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final StringRedisTemplate redisTemplate;
    private final JwtProperties jwtProperties;

    @Override
    public void save(String email, String refreshToken) {
        redisTemplate.opsForValue().set(
            "refreshToken:" + email,
            refreshToken,
            jwtProperties.getRefreshTokenExpiration(),
            TimeUnit.SECONDS
        );
    }

    @Override
    public Optional<String> findByEmail(String email) {
        String token = redisTemplate.opsForValue().get("refreshToken:" + email);
        return Optional.ofNullable(token);
    }

    @Override
    public void deleteByEmail(String email) {
        redisTemplate.delete("refreshToken:" + email);
    }
}
