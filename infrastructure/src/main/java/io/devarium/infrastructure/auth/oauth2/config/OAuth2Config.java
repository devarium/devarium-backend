package io.devarium.infrastructure.auth.oauth2.config;

import io.devarium.core.domain.user.port.OAuth2Client;
import io.devarium.infrastructure.auth.oauth2.GoogleOAuth2Client;
import io.devarium.infrastructure.auth.oauth2.GoogleOAuth2Properties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@EnableConfigurationProperties(GoogleOAuth2Properties.class)
@Configuration
public class OAuth2Config {

    @Bean
    public OAuth2Client googleOAuth2Client(WebClient webClient, GoogleOAuth2Properties properties) {
        return new GoogleOAuth2Client(webClient, properties);
    }
}
