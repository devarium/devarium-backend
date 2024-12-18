package io.devarium.api.controller.auth;

import io.devarium.api.auth.dto.TokenResponse;
import io.devarium.api.common.dto.SingleItemResponse;
import io.devarium.core.auth.Token;
import io.devarium.core.auth.constants.JwtConstants;
import io.devarium.core.auth.exception.AuthErrorCode;
import io.devarium.core.auth.exception.CustomAuthException;
import io.devarium.core.auth.service.AuthService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@RequestMapping("/api/${api.version}/auth")
@RestController
public class AuthController {

    private final AuthService authService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    @GetMapping("/google/callback")
    public ResponseEntity<?> handleGoogleCallback(@RequestParam String code) {
        // 1. Google 토큰 요청 엔드포인트 설정
        String tokenEndpoint = "https://oauth2.googleapis.com/token";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        // 2. 토큰 요청 및 응답 처리
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
            tokenEndpoint,
            HttpMethod.POST,
            request,
            new ParameterizedTypeReference<>() {
            }
        );

        Map<String, Object> body = response.getBody();

        String accessToken = null;
        String idToken = null;
        if (body != null && body.containsKey("access_token")) {
            accessToken = (String) body.get("access_token");
            idToken = (String) body.get("id_token");

            // 3. 사용자 정보 요청
            String userInfoEndpoint = "https://www.googleapis.com/oauth2/v1/userinfo";

            HttpHeaders authHeader = new HttpHeaders();
            authHeader.setBearerAuth(accessToken);

            HttpEntity<Void> userInfoRequest = new HttpEntity<>(authHeader);

            ResponseEntity<Map<String, Object>> userInfoResponse = restTemplate.exchange(
                userInfoEndpoint,
                HttpMethod.GET,
                userInfoRequest,
                new ParameterizedTypeReference<>() {
                }
            );

            Map<String, Object> userInfo = userInfoResponse.getBody();
            if (userInfo == null) {
                throw new CustomAuthException(AuthErrorCode.USER_NOT_FOUND);
            }

            // 3. JWT 발급
            Token token = authService.login(userInfo, "google");

            // 4. 서버 토큰 반환
            return ResponseEntity.ok(token);
        }

        throw new CustomAuthException(AuthErrorCode.GOOGLE_ACCESS_TOKEN_IS_NULL);
    }

    @PostMapping("/refresh")
    public ResponseEntity<SingleItemResponse<TokenResponse>> refresh(
        @RequestHeader(JwtConstants.REFRESH_HEADER) String refreshToken
    ) {
        return ResponseEntity.ok(
            SingleItemResponse.from(TokenResponse.from(authService.refresh(refreshToken)))
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        authService.logout();
        return ResponseEntity.noContent().build();
    }
}