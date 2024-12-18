package io.devarium.core.auth.service;

import io.devarium.core.auth.Token;
import io.devarium.core.auth.command.UserDetailsInterface;
import io.devarium.core.auth.exception.AuthErrorCode;
import io.devarium.core.auth.exception.CustomAuthException;
import io.devarium.core.domain.user.User;
import io.devarium.core.domain.user.service.UserService;
import java.util.Map;
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

    public Token login(Map<String, Object> userInfo, String provider) {

        String email = (String) userInfo.get("email");
        if (email == null) {
            throw new CustomAuthException(AuthErrorCode.USER_NOT_FOUND);
        }

        User user = userService.getUser(email);
        if (user == null) {
            user = userService.createUser(userInfo, provider);
        } else {
            userService.updateUserInfo(user, userInfo);
        }

        return tokenService.generateTokens(email, user.getAuthorities());
    }

    public Token refresh(String refreshToken) {
        return tokenService.refreshTokens(refreshToken);
    }

    public void logout() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetailsInterface userDetails) {
            String username = userDetails.getUsername();

            // 리프레시 토큰 삭제 요청
            tokenService.deleteRefreshTokenByUsername(username);

            log.info("User logged out successfully: {}", username);
        } else {
            log.warn("Unauthenticated user attempted to log out.");
            throw new CustomAuthException(AuthErrorCode.UNAUTHENTICATED_USER);
        }

        // SecurityContext 초기화
        SecurityContextHolder.clearContext();
    }
}
