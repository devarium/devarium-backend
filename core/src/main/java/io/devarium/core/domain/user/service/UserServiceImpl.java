package io.devarium.core.domain.user.service;

import io.devarium.core.auth.OAuth2UserInfo;
import io.devarium.core.domain.user.User;
import io.devarium.core.domain.user.UserRole;
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
    public User getUser(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public User updateUserInfo(User user, OAuth2UserInfo userInfo) {
        user.update(userInfo.name(), userInfo.picture());
        return userRepository.save(user);
    }

    @Override
    public User updateUserProfile(String email, UpdateUser command) {
        User user = getUser(email);
        user.update(command.blogUrl(), command.githubUrl(), command.content());
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(String email) {
        User user = getUser(email);
        user.delete();
        userRepository.save(user);
    }
}
