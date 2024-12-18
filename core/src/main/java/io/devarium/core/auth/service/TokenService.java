package io.devarium.core.auth.service;

import io.devarium.core.auth.Token;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

public interface TokenService {

    Token generateTokens(String username, Collection<? extends GrantedAuthority> authorities);

    Token refreshTokens(String refreshToken);

}
