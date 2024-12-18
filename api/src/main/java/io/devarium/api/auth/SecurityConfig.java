package io.devarium.api.auth;

import io.devarium.api.auth.filter.JwtAuthenticationFilter;
import io.devarium.api.auth.handler.JwtAccessDeniedHandler;
import io.devarium.api.auth.handler.JwtAuthenticationEntryPoint;
import io.devarium.infrastructure.security.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/api/v1/auth/google/callback/**", "/error",
                    "/favicon.ico")
                .permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .defaultSuccessUrl("/loginSuccess", true)
                .failureUrl("/loginFailure")
            ) // OAuth 2.0 로그인 지원 활성화
            .formLogin(AbstractHttpConfigurer::disable) // 폼 로그인 비활성화
            .exceptionHandling(it -> it
                .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 인증 실패 핸들러
                .accessDeniedHandler(jwtAccessDeniedHandler) // 접근 거부 핸들러
            )
            .sessionManagement(it -> it
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 비활성화 (Stateless)
            )
            .addFilterBefore(
                new JwtAuthenticationFilter(jwtUtil, userDetailsService), // JWT 필터 추가
                UsernamePasswordAuthenticationFilter.class
            );
        return http.build();
    }
}
