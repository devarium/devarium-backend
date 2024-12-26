package io.devarium.infrastructure.persistence.repository;

import io.devarium.core.domain.user.User;
import io.devarium.core.domain.user.exception.UserErrorCode;
import io.devarium.core.domain.user.exception.UserException;
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
        return savedEntity.toDomain();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(UserEntity::toDomain);
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userJpaRepository.findById(userId).map(UserEntity::toDomain);
    }

    public UserEntity convertToEntity(User domain) {
        if (domain.getId() != null) {
            UserEntity entity = userJpaRepository.findByEmail(domain.getEmail())
                .orElseThrow(
                    () -> new UserException(UserErrorCode.USER_EMAIL_NOT_FOUND, domain.getEmail()));
            entity.update(domain);
            return entity;
        }
        return UserEntity.builder()
            .email(domain.getEmail())
            .name(domain.getName())
            .picture(domain.getPicture())
            .role(domain.getRole())
            .provider(domain.getProvider())
            .build();
    }
}
