package io.devarium.api.controller.auth;

import io.devarium.api.auth.CustomUserDetails;
import io.devarium.api.common.dto.SingleItemResponse;
import io.devarium.api.controller.auth.dto.TokenResponse;
import io.devarium.core.auth.Token;
import io.devarium.core.auth.service.AuthService;
import io.devarium.infrastructure.auth.jwt.JwtConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/${api.version}/auth")
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/refresh")
    public ResponseEntity<SingleItemResponse<TokenResponse>> refresh(
        @RequestHeader(JwtConstants.REFRESH_HEADER) String refreshToken
    ) {
        Token token = authService.refresh(refreshToken);
        TokenResponse response = TokenResponse.from(token);

        return ResponseEntity.ok(SingleItemResponse.from(response));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null
            && authentication.getPrincipal() instanceof CustomUserDetails principal) {
            String email = principal.getEmail();
            authService.logout(email);
            SecurityContextHolder.clearContext();
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthenticated user");
    }
}
