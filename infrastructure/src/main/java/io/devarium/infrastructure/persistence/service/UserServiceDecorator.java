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
    public User getUserByEmail(String email) {
        return userService.getUserByEmail(email);
    }

    @Override
    @Transactional
    public User updateUserInfo(User user, OAuth2UserInfo userInfo) {
        return userService.updateUserInfo(user, userInfo);
    }

    @Override
    @Transactional
    public User updateUserProfile(Long userId, UpdateUser command) {
        return userService.updateUserProfile(userId, command);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        userService.deleteUser(userId);
    }
}
