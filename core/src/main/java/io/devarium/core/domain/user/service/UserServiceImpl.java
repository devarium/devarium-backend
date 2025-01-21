package io.devarium.core.domain.user.service;

import io.devarium.core.auth.OAuth2UserInfo;
import io.devarium.core.domain.user.User;
import io.devarium.core.domain.user.UserRole;
import io.devarium.core.domain.user.exception.UserErrorCode;
import io.devarium.core.domain.user.exception.UserException;
import io.devarium.core.domain.user.port.UpdateUser;
import io.devarium.core.domain.user.repository.UserRepository;
import io.devarium.core.storage.Image;
import io.devarium.core.storage.ImageType;
import io.devarium.core.storage.service.StorageService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final StorageService storageService;

    @Override
    public User createUser(OAuth2UserInfo userInfo) {
        User user = User.builder()
            .email(userInfo.email())
            .username(userInfo.name())
            .profileImageUrl(userInfo.profileImageUrl())
            .provider(userInfo.provider())
            .role(UserRole.USER)
            .build();
        return userRepository.save(user);
    }

    @Override
    public User getUser(Long userId) {
        return userRepository.findById(userId)
            //TODO: 소프트딜리트 고려 .filter(user -> !user.isDeleted())
            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND, userId));
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UserException(UserErrorCode.USER_EMAIL_NOT_FOUND, email));
    }

    @Override
    public User updateUserProfile(UpdateUser request, User user) {
        user.update(request.username(), request.bio(), request.blogUrl(), request.githubUrl());
        return userRepository.save(user);
    }

    @Override
    public User updateUserProfileImage(Image image, User user) {
        storageService.delete(user.getProfileImageUrl());
        String newImageUrl = storageService.upload(image, ImageType.PROFILE);
        user.update(newImageUrl);
        return userRepository.save(user);
    }

    @Override
    public void withdraw(User user) {
        user.delete();
        userRepository.save(user);
    }
}
