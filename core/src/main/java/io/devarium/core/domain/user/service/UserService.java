package io.devarium.core.domain.user.service;

import io.devarium.core.auth.OAuth2UserInfo;
import io.devarium.core.domain.user.User;
import io.devarium.core.domain.user.port.UpdateUser;

public interface UserService {

    User createUser(OAuth2UserInfo userInfo);

    User getUser(Long userId);

    User getUserByEmail(String email);

    User updateUserInfo(User user, OAuth2UserInfo userInfo);

    User updateUserProfile(Long userId, UpdateUser request);

    void deleteUser(Long userId);
}
