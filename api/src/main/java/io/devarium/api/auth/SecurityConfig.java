package io.devarium.api.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/","/api/v1/auth/google/callback").permitAll()
                .anyRequest().authenticated()
            );
//            .oauth2Login(oauth2 -> oauth2
//                .defaultSuccessUrl("/home", true) // 성공 시 리디렉션 URL
//                .failureUrl("/login?error=true")  // 로그인 실패 시 URL
//            ); // OAuth 2.0 로그인 지원 활성화
        return http.build();
    }
}
