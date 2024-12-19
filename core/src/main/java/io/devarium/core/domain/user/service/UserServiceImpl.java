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
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UserException(UserErrorCode.USER_EMAIL_NOT_FOUND, email));
    }

    @Override
    public User updateUserInfo(User user, OAuth2UserInfo userInfo) {
        user.update(userInfo.name(), userInfo.picture());
        return userRepository.save(user);
    }

    @Override
    public User updateUserProfile(Long userId, UpdateUser request) {
        User user = getUser(userId);
        user.update(request.blogUrl(), request.githubUrl(), request.content());
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = getUser(userId);
        user.delete();
        userRepository.save(user);
    }
}
