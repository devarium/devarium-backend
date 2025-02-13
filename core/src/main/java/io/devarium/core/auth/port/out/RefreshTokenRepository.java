package io.devarium.core.auth.port.out;

import java.util.Optional;

public interface RefreshTokenRepository {

    void save(String email, String refreshToken);

    Optional<String> findByEmail(String email);

    void deleteByEmail(String email);
}
