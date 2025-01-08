package io.devarium.api.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.devarium.api.auth.CustomUserDetails;
import io.devarium.api.controller.auth.dto.TokenResponse;
import io.devarium.core.auth.Token;
import io.devarium.core.auth.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final TokenService tokenService;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {
        CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();

        String email = userPrincipal.getEmail();
        Collection<? extends GrantedAuthority> authorities = userPrincipal.getAuthorities();

        // TokenService를 활용해 JWT 생성
        Token token = tokenService.generateTokens(email, authorities);

        // TokenResponse 생성
        TokenResponse tokenResponse = TokenResponse.from(token);

        // JSON 직렬화 및 응답
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(tokenResponse));
    }
}
