package io.devarium.infrastructure.auth.jwt;

import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@EnableConfigurationProperties(JwtProperties.class)
@Configuration
public class JwtConfig {

    private final JwtProperties jwtProperties;

    @Bean
    public Key key() {
        byte[] bytes = Base64.getDecoder().decode(jwtProperties.getSecret());
        return Keys.hmacShaKeyFor(bytes);
    }
}

