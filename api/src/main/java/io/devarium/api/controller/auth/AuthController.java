package io.devarium.api.controller.auth;

import io.devarium.api.common.dto.SingleItemResponse;
import io.devarium.api.controller.auth.dto.TokenResponse;
import io.devarium.core.auth.Token;
import io.devarium.core.auth.service.AuthService;
import io.devarium.infrastructure.security.jwt.constants.JwtConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/${api.version}/auth")
@RestController
public class AuthController {

    private final AuthService authService;

    @GetMapping("/google/callback")
    public ResponseEntity<SingleItemResponse<TokenResponse>> handleGoogleCallback(
        @RequestParam String code
    ) {
        Token token = authService.loginWithGoogle(code);
        TokenResponse response = TokenResponse.from(token);

        return ResponseEntity.ok(SingleItemResponse.from(response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<SingleItemResponse<TokenResponse>> refresh(
        @RequestHeader(JwtConstants.REFRESH_HEADER) String refreshToken
    ) {
        Token token = authService.refresh(refreshToken);
        TokenResponse response = TokenResponse.from(token);

        return ResponseEntity.ok(SingleItemResponse.from(response));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        authService.logout();
        return ResponseEntity.noContent().build();
    }
}
