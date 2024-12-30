package io.devarium.infrastructure.auth.oauth2;

import static io.devarium.infrastructure.auth.jwt.JwtConstants.BEARER_PREFIX;

import io.devarium.core.auth.OAuth2Client;
import io.devarium.core.auth.OAuth2Provider;
import io.devarium.core.auth.OAuth2UserInfo;
import io.devarium.core.auth.exception.AuthErrorCode;
import io.devarium.core.auth.exception.CustomAuthException;
import io.devarium.infrastructure.auth.oauth2.dto.GoogleTokenResponse;
import io.devarium.infrastructure.auth.oauth2.dto.GoogleUserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GoogleOAuth2Client implements OAuth2Client {

    private final WebClient webClient;
    private final GoogleOAuth2Properties properties;

    @Override
    public OAuth2UserInfo getUserInfo(String code) {
        String accessToken = requestGoogleToken(code).accessToken();
        GoogleUserInfoResponse userInfo = requestGoogleUserInfo(accessToken);

        return OAuth2UserInfo.of(
            userInfo.id(),
            userInfo.email(),
            userInfo.name(),
            userInfo.profileImageUrl(),
            OAuth2Provider.GOOGLE
        );
    }

    private GoogleTokenResponse requestGoogleToken(String code) {
        return webClient.post()
            .uri(properties.getTokenEndpoint())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData(createTokenRequest(code)))
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, this::handle4xxError)
            .onStatus(HttpStatusCode::is5xxServerError, this::handle5xxError)
            .bodyToMono(GoogleTokenResponse.class)
            .blockOptional()
            .orElseThrow(() -> new CustomAuthException(AuthErrorCode.GOOGLE_TOKEN_REQUEST_FAILED));
    }

    private GoogleUserInfoResponse requestGoogleUserInfo(String accessToken) {
        return webClient.get()
            .uri(properties.getUserInfoEndpoint())
            .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + accessToken)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, this::handle4xxError)
            .onStatus(HttpStatusCode::is5xxServerError, this::handle5xxError)
            .bodyToMono(GoogleUserInfoResponse.class)
            .blockOptional()
            .orElseThrow(
                () -> new CustomAuthException(AuthErrorCode.GOOGLE_USER_INFO_REQUEST_FAILED)
            );
    }

    private MultiValueMap<String, String> createTokenRequest(String code) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("client_id", properties.getClientId());
        formData.add("client_secret", properties.getClientSecret());
        formData.add("redirect_uri", properties.getRedirectUri());
        formData.add("grant_type", "authorization_code");

        return formData;
    }

    private Mono<? extends Throwable> handle4xxError(ClientResponse response) {
        if (response.statusCode() == HttpStatus.UNAUTHORIZED) {
            return Mono.error(new CustomAuthException(AuthErrorCode.GOOGLE_INVALID_TOKEN));
        }
        return Mono.error(
            new CustomAuthException(
                AuthErrorCode.GOOGLE_TOKEN_REQUEST_FAILED,
                response.statusCode().toString()
            )
        );
    }

    private Mono<? extends Throwable> handle5xxError(ClientResponse response) {
        return Mono.error(
            new CustomAuthException(
                AuthErrorCode.GOOGLE_SERVER_ERROR,
                response.statusCode().toString()
            )
        );
    }
}
