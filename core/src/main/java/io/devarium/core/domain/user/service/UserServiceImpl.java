package io.devarium.core.domain.user.service;

import io.devarium.core.auth.OAuth2UserInfo;
import io.devarium.core.domain.user.User;
import io.devarium.core.domain.user.UserRole;
import io.devarium.core.domain.user.exception.UserErrorCode;
import io.devarium.core.domain.user.exception.UserException;
import io.devarium.core.domain.user.port.UpdateUser;
import io.devarium.core.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User createUser(OAuth2UserInfo userInfo) {
        User user = User.builder()
            .email(userInfo.email())
            .name(userInfo.name())
            .picture(userInfo.picture())
            .provider(userInfo.provider())
            .role(UserRole.USER)
            .build();
        return userRepository.save(user);
    }

    @Override
    public User getUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND, userId));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public User updateUserInfo(OAuth2UserInfo userInfo, User user) {
        user.update(userInfo.name(), userInfo.picture());
        return userRepository.save(user);
    }

    @Override
    public User updateUserProfile(UpdateUser request, User user) {
        user.update(request.bio(), request.blogUrl(), request.githubUrl());
        return userRepository.save(user);
    }

    @Override
    public void withdraw(User user) {
        user.delete();
        userRepository.save(user);
    }
}
