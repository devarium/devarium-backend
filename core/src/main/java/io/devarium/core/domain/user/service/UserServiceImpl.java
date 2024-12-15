package io.devarium.core.domain.user.service;

import io.devarium.core.domain.user.User;
import io.devarium.core.domain.user.command.UpdateUserCommand;
import io.devarium.core.domain.user.exception.UserErrorCode;
import io.devarium.core.domain.user.exception.UserException;
import io.devarium.core.domain.user.repository.UserRepository;
import java.time.Instant;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User createUser(Map<String, Object> userInfo) {
        String name = (String) userInfo.get("name");
        String email = (String) userInfo.get("email");
        String picture = (String) userInfo.get("picture");
        User user = User.builder()
            .name(name)
            .email(email)
            .picture(picture)
            .build();
        return userRepository.save(user);
    }

    @Override
    public User getUser(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UserException(UserErrorCode.USER_EMAIL_NOT_FOUND, email));
    }

    @Override
    public User updateUserInfo(User user, Map<String, Object> userInfo) {
        String name = (String) userInfo.get("name");
        String picture = (String) userInfo.get("picture");
        user.update(name, picture);
        return userRepository.save(user);
    }

    @Override
    public User updateUserProfile(String email, UpdateUserCommand command){
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
