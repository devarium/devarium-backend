package io.devarium.api.auth.filter;

import io.devarium.infrastructure.auth.jwt.JwtConstants;
import io.devarium.infrastructure.auth.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j(topic = "JwtAuthenticationFilter")
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String token = extractToken(request);

        try {
            if (token != null && jwtUtil.isTokenValid(token)) {
                String email = jwtUtil.extractEmail(token);
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
                //TODO: 사용자 정보 로드 (소프트 딜리트 확인 포함)
                /*if (userDetails instanceof CustomUserDetails customUserDetails) {
                    if (customUserDetails.getUser().isDeleted()) {
                        throw new IllegalStateException("User is soft deleted: " + email);
                    }
                }*/

                Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                    );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            log.error("Failed to set user authentication in security context", e);
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String token = request.getHeader(JwtConstants.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(token)) {
            return jwtUtil.resolveToken(token);
        }
        return null;
    }
}
