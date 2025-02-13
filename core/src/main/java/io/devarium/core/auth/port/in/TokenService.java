package io.devarium.core.auth.port.in;

import io.devarium.core.auth.Token;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

public interface TokenService {

    Token generateTokens(String email, Collection<? extends GrantedAuthority> authorities);

    Token refreshTokens(String refreshToken);

    void deleteRefreshTokenByEmail(String email);
}
