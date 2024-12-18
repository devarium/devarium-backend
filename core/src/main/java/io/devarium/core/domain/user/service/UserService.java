package io.devarium.core.domain.user.service;

import io.devarium.core.domain.user.OAuth2UserInfo;
import io.devarium.core.domain.user.User;
import io.devarium.core.domain.user.command.UpdateUserCommand;

public interface UserService {

    User createUser(OAuth2UserInfo userInfo);

    User getUser(String email);

    User updateUserInfo(User user, OAuth2UserInfo userInfo);

    User updateUserProfile(String email, UpdateUserCommand command);

    void deleteUser(String email);
}
