package io.devarium.api.auth.handler;

import io.devarium.infrastructure.auth.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        Collection<? extends GrantedAuthority> authorities = oAuth2User.getAuthorities();

        // JWT 생성
        String accessToken = jwtUtil.generateAccessToken(email, authorities);
        String refreshToken = jwtUtil.generateRefreshToken(email, authorities);

        // 응답에 추가
        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setHeader("Refresh-Token", refreshToken);

        response.setContentType("application/json");
        response.getWriter().write(
            String.format("{\"accessToken\": \"%s\", \"refreshToken\": \"%s\"}", accessToken,
                refreshToken));
    }
}
