package io.devarium.core.domain.user.service;

import io.devarium.core.auth.OAuth2UserInfo;
import io.devarium.core.domain.user.User;
import io.devarium.core.domain.user.port.UpdateUser;

public interface UserService {

    User createUser(OAuth2UserInfo userInfo);

    User getUser(String email);

    User updateUserInfo(User user, OAuth2UserInfo userInfo);

    User updateUserProfile(String email, UpdateUser command);

    void deleteUser(String email);
}
