package io.devarium.core.auth.port.in;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

public interface TokenProvider {

    String generateAccessToken(String email, Collection<? extends GrantedAuthority> authorities);

    String generateRefreshToken(String email, Collection<? extends GrantedAuthority> authorities);

    String extractEmail(String email);

    Collection<? extends GrantedAuthority> extractAuthorities(String token);

    String resolveToken(String token);

    boolean isTokenValid(String token);
}
