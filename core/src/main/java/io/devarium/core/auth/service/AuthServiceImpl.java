package io.devarium.core.auth.service;

import io.devarium.core.auth.exception.AuthErrorCode;
import io.devarium.core.auth.exception.CustomAuthException;
import io.devarium.core.domain.user.User;
import io.devarium.core.domain.user.service.UserService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl {

    private final UserService userService;

    public void login(Map<String, Object> userInfo) {

        String email = (String) userInfo.get("email");
        if (email == null) {
            throw new CustomAuthException(AuthErrorCode.USER_NOT_FOUND);
        }

        User user = userService.getUser(email);
        if (user == null) {
            userService.createUser(userInfo);
        } else {
            userService.updateUserInfo(user, userInfo);
        }
    }
}
