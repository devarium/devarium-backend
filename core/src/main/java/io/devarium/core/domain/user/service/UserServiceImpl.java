package io.devarium.core.domain.user.service;

import io.devarium.core.domain.user.OAuth2UserInfo;
import io.devarium.core.domain.user.User;
import io.devarium.core.domain.user.UserRole;
import io.devarium.core.domain.user.command.UpdateUserCommand;
import io.devarium.core.domain.user.repository.UserRepository;
import java.time.Instant;
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
    public User updateUserProfile(String email, UpdateUserCommand command) {
        User user = getUser(email);
        user.update(command.blogUrl(), command.githubUrl(), command.content());
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(String email) {
        User user = getUser(email);
        user.delete(Instant.now());
        userRepository.save(user);
    }
}
