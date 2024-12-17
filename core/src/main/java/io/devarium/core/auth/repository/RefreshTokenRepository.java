package io.devarium.core.auth.repository;

import java.util.Optional;

public interface RefreshTokenRepository {
    void save(String username, String refreshToken);
    Optional<String> findByEmail(String email);
    void deleteByEmail(String email);
}
