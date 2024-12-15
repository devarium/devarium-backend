package io.devarium.core.domain.user.service;

import io.devarium.core.domain.user.User;
import io.devarium.core.domain.user.command.UpdateUserCommand;
import java.util.Map;

public interface UserService {

    User createUser(Map<String, Object> userInfo, String provider);

    User getUser(String email);

    User updateUserInfo(User user, Map<String, Object> userInfo);

    User updateUserProfile(String email, UpdateUserCommand command);

    void deleteUser(String email);
}
