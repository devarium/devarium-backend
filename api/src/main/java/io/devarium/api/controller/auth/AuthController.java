package io.devarium.api.controller.auth;

import static io.devarium.core.auth.AuthConstants.REFRESH_HEADER;

import io.devarium.api.auth.CustomUserPrincipal;
import io.devarium.api.common.dto.SingleItemResponse;
import io.devarium.api.controller.auth.dto.TokenResponse;
import io.devarium.core.auth.Token;
import io.devarium.core.auth.exception.AuthErrorCode;
import io.devarium.core.auth.exception.CustomAuthException;
import io.devarium.core.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
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
        @RequestHeader(REFRESH_HEADER) String refreshToken
    ) {
        Token token = authService.refresh(refreshToken);
        TokenResponse response = TokenResponse.from(token);

        return ResponseEntity.ok(SingleItemResponse.from(response));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null
            && authentication.getPrincipal() instanceof CustomUserPrincipal principal) {
            String email = principal.getEmail();
            authService.logout(email);
            SecurityContextHolder.clearContext();
            return ResponseEntity.noContent().build();
        }
        //TODO: 블랙리스트 구현시 로직이 달라질 수 있음. 블랙리스트 구현

        throw new CustomAuthException(AuthErrorCode.UNAUTHENTICATED_USER);
    }
}
