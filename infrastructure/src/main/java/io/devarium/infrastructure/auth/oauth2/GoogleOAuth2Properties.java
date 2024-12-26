package io.devarium.infrastructure.auth.oauth2;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "oauth2.google")
public class GoogleOAuth2Properties {

    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final String tokenEndpoint;
    private final String userInfoEndpoint;
}
