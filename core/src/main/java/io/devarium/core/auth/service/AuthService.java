package io.devarium.core.auth.service;

import io.devarium.core.auth.EmailPrincipal;
import io.devarium.core.auth.OAuth2Client;
import io.devarium.core.auth.OAuth2UserInfo;
import io.devarium.core.auth.Token;
import io.devarium.core.auth.exception.AuthErrorCode;
import io.devarium.core.auth.exception.CustomAuthException;
import io.devarium.core.domain.user.User;
import io.devarium.core.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserService userService;
    private final TokenService tokenService;
    private final OAuth2Client oAuth2Client;

    public Token loginWithGoogle(String code) {
        OAuth2UserInfo userInfo = oAuth2Client.getUserInfo(code);
        User user = userService.getUserByEmail(userInfo.email());

        if (user == null) {
            user = userService.createUser(userInfo);
        } else {
            userService.updateUserInfo(userInfo, user);
        }

        return tokenService.generateTokens(user.getEmail(), user.getAuthorities());
    }

    public Token refresh(String refreshToken) {
        return tokenService.refreshTokens(refreshToken);
    }

    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null &&
            authentication.getPrincipal() instanceof EmailPrincipal principal
        ) {
            String email = principal.getEmail();
            tokenService.deleteRefreshTokenByEmail(email);
            SecurityContextHolder.clearContext();
            log.info("User logged out successfully: {}", email);
        } else {
            log.warn("Unauthenticated user attempted to log out");
            SecurityContextHolder.clearContext();
            throw new CustomAuthException(AuthErrorCode.UNAUTHENTICATED_USER);
        }
    }
}
