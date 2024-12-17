package io.devarium.core.auth.service;

import io.devarium.core.auth.Token;
import io.devarium.core.auth.exception.AuthErrorCode;
import io.devarium.core.auth.exception.CustomAuthException;
import io.devarium.core.domain.user.User;
import io.devarium.core.domain.user.service.UserService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
            user=userService.createUser(userInfo,provider);
        } else {
            userService.updateUserInfo(user, userInfo);
        }

        return tokenService.generateTokens(email, user.getAuthorities());
    }
}
