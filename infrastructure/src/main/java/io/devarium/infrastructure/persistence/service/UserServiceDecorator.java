package io.devarium.infrastructure.persistence.service;

import io.devarium.core.auth.OAuth2UserInfo;
import io.devarium.core.domain.user.User;
import io.devarium.core.domain.user.port.UpdateUser;
import io.devarium.core.domain.user.service.UserService;
import io.devarium.core.domain.user.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class UserServiceDecorator implements UserService {

    private final UserServiceImpl userService;

    @Override
    @Transactional
    public User createUser(OAuth2UserInfo userInfo) {
        return userService.createUser(userInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(Long userId) {
        return userService.getUser(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public User getByEmail(String email) {
        return userService.getByEmail(email);
    }

    @Override
    @Transactional
    public User updateUserInfo(OAuth2UserInfo userInfo, User user) {
        return userService.updateUserInfo(userInfo, user);
    }

    @Override
    @Transactional
    public User updateUserProfile(UpdateUser request, User user) {
        return userService.updateUserProfile(request, user);
    }

    @Override
    @Transactional
    public void withdraw(User user) {
        userService.withdraw(user);
    }
}
