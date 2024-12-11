package io.devarium.infrastructure.persistence.repository;

import io.devarium.core.domain.user.User;
import io.devarium.core.domain.user.repository.UserRepository;
import io.devarium.infrastructure.persistence.entity.UserEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public User save(User user) {
        UserEntity entity = convertToEntity(user);
        UserEntity savedEntity = userJpaRepository.save(entity);
        return convertToDomain(savedEntity);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(this::convertToDomain);
    }

    public User convertToDomain(UserEntity entity) {
        return User.builder()
            .id(entity.getId())
            .email(entity.getEmail())
            .nickname(entity.getNickname())
            .role(entity.getRole())
            //.provider(entity.getProvider())
            //.deletedAt(entity.getDeletedAt())
            .build();
    }

    public UserEntity convertToEntity(User user) {
        return UserEntity.builder()
            .email(user.getEmail())
            .nickname(user.getNickname())
            .role(user.getRole())
            //.provider(user.getProvider())
            //.deletedAt(user.getDeletedAt())
            .build();
    }

}
