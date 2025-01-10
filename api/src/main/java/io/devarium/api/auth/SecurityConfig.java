package io.devarium.api.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.devarium.api.auth.filter.JwtAuthenticationFilter;
import io.devarium.api.auth.handler.JwtAccessDeniedHandler;
import io.devarium.api.auth.handler.JwtAuthenticationEntryPoint;
import io.devarium.api.auth.handler.OAuth2AuthenticationSuccessHandler;
import io.devarium.core.auth.service.TokenService;
import io.devarium.infrastructure.auth.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtUtil jwtUtil;
    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService;
    private final ObjectMapper objectMapper;
    private final TokenService tokenService;
    private final UserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(
                        "/",
                        "/login",
                        "/oauth2/**",
                        "/error",
                        "/favicon.ico"
                    ).permitAll()
                    .anyRequest().authenticated()
                //.anyRequest().permitAll()
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .defaultSuccessUrl("/loginSuccess", true)
                .userInfoEndpoint(userInfo -> userInfo.userService(
                    customOAuth2UserService)) // CustomOAuth2UserService 설정
                .successHandler(oAuth2AuthenticationSuccessHandler()) // OAuth2 로그인 성공 핸들러 추가
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
                new JwtAuthenticationFilter(jwtUtil, customUserDetailsService), // JWT 필터 추가
                UsernamePasswordAuthenticationFilter.class
            );
        return http.build();
    }

    @Bean
    public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return new OAuth2AuthenticationSuccessHandler(tokenService, objectMapper);
    }
}
