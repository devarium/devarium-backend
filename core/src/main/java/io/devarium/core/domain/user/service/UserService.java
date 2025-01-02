package io.devarium.core.domain.user.service;

import io.devarium.core.auth.OAuth2UserInfo;
import io.devarium.core.domain.user.User;
import io.devarium.core.domain.user.port.UpdateUser;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    User createUser(OAuth2UserInfo userInfo);

    User getUser(Long userId);

    User getByEmail(String email);

    User updateUserProfile(UpdateUser request, User user);

    User updateUserProfileImageUrl(MultipartFile image, User user);

    void withdraw(User user);
}
