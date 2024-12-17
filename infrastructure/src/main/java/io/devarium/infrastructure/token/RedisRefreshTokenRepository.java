package io.devarium.infrastructure.token;


import io.devarium.core.auth.repository.RefreshTokenRepository;
import io.devarium.infrastructure.security.jwt.properties.JwtProperties;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class RedisRefreshTokenRepository implements RefreshTokenRepository {

    private final StringRedisTemplate redisTemplate;
    //private static final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60; // 7 days
    private final JwtProperties jwtProperties;

    @Override
    public void save(String username, String refreshToken) {
        redisTemplate.opsForValue().set(
            "refreshToken:" + username,
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

