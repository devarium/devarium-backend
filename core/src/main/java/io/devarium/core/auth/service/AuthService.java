package io.devarium.core.auth.service;

import io.devarium.core.auth.Token;
import io.devarium.core.auth.command.UserDetailsInterface;
import io.devarium.core.auth.exception.AuthErrorCode;
import io.devarium.core.auth.exception.CustomAuthException;
import io.devarium.core.domain.user.OAuth2UserInfo;
import io.devarium.core.domain.user.User;
import io.devarium.core.domain.user.port.OAuth2Client;
import io.devarium.core.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        User user = userService.getUser(userInfo.email());

        if (user == null) {
            user = userService.createUser(userInfo);
        } else {
            userService.updateUserInfo(user, userInfo);
        }

        return tokenService.generateTokens(user.getEmail(), user.getAuthorities());
    }

    public Token refresh(String refreshToken) {
        return tokenService.refreshTokens(refreshToken);
    }

    public void logout() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetailsInterface userDetails) {
            String username = userDetails.getUsername();

            tokenService.deleteRefreshTokenByUsername(username);

            log.info("User logged out successfully: {}", username);
        } else {
            log.warn("Unauthenticated user attempted to log out.");
            throw new CustomAuthException(AuthErrorCode.UNAUTHENTICATED_USER);
        }

        SecurityContextHolder.clearContext();
    }
}
