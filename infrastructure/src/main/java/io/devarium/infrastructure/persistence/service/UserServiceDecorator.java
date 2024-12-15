package io.devarium.infrastructure.persistence.service;

import io.devarium.core.domain.user.User;
import io.devarium.core.domain.user.command.UpdateUserCommand;
import io.devarium.core.domain.user.service.UserService;
import io.devarium.core.domain.user.service.UserServiceImpl;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class UserServiceDecorator implements UserService {

    private final UserServiceImpl userService;

    @Override
    @Transactional
    public User createUser(Map<String, Object> userInfo) {
        return userService.createUser(userInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(String email) {
        return userService.getUser(email);
    }

    @Override
    @Transactional
    public User updateUserInfo(User user, Map<String, Object> userInfo){
        return userService.updateUserInfo(user, userInfo);
    }

    @Override
    @Transactional
    public User updateUserProfile(String email, UpdateUserCommand command){
        return userService.updateUserProfile(email,command);
    }

    @Override
    @Transactional
    public void deleteUser(String email) {
        userService.deleteUser(email);
    }
}
