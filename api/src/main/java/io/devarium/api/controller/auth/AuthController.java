package io.devarium.api.controller.auth;

import io.devarium.core.auth.exception.AuthErrorCode;
import io.devarium.core.auth.exception.CustomAuthException;
import io.devarium.core.auth.service.AuthServiceImpl;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@RequestMapping("/api/${api.version}/auth")
@RestController
public class AuthController {

    private final AuthServiceImpl authService;
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
            new ParameterizedTypeReference<>() {}
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
                new ParameterizedTypeReference<>() {}
            );

            Map<String, Object> userInfo = userInfoResponse.getBody();

            authService.login(userInfo);

            return ResponseEntity.ok(userInfo);
        }

        throw new CustomAuthException(AuthErrorCode.GOOGLE_ACCESS_TOKEN_IS_NULL);
    }

/*
    @GetMapping("/google/callback")
    public ResponseEntity<SingleItemResponse<AuthResponse>> handleGoogleCallback(
        @RequestParam("code") String code
    ) {
        // 사용자 인증 처리 (구글 API 호출은 Core에서 숨겨짐)
        AuthResponse authResponse = AuthService.authenticateWithGoogle(code);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(SingleItemResponse.from(authResponse));
    }

    //소셜 로그인 처리(구글)
    @PostMapping("/google")
    public ResponseEntity<SingleItemResponse<AuthResponse>> loginWithGoogle(
        @RequestBody GoogleLoginRequest googleLoginRequest
    ) {
        // AuthService를 호출해 로그인 처리
        AuthResponse authResponse = authService.loginWithGoogle(googleLoginRequest.getAccessToken());

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(SingleItemResponse.from(authResponse));
    }

    @GetMapping("/me")
    public ResponseEntity<SingleItemResponse<UserResponse>> getCurrentUser(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        // 인증된 사용자 정보 반환
        UserResponse userResponse = authService.getCurrentUser(userDetails.getEmail());

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(SingleItemResponse.from(userResponse));
    }

        @GetMapping("/google/callback")
    public ResponseEntity<?> handleGoogleCallbackGet(@RequestParam String code) {
        // GET 요청 처리 후 내부적으로 POST 로직 호출
        return handleGoogleCallbackPost(code);
    }

    @PostMapping("/google/callback")
    public ResponseEntity<?> handleGoogleCallbackPost(@RequestParam String code) {
*/

}
