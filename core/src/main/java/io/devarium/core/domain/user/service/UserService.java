package io.devarium.core.domain.user.service;

import io.devarium.core.auth.OAuth2UserInfo;
import io.devarium.core.domain.user.User;
import io.devarium.core.domain.user.port.UpdateUser;

public interface UserService {

    User createUser(OAuth2UserInfo userInfo);

    User getUser(Long userId);

    User getOrCreateUser(OAuth2UserInfo userInfo);

    User updateUserInfo(OAuth2UserInfo userInfo, User user);

    User updateUserProfile(UpdateUser request, User user);

    void withdraw(User user);
}
