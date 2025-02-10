package io.devarium.infrastructure.persistence.repository;

import io.devarium.core.domain.user.User;
import io.devarium.core.domain.user.exception.UserErrorCode;
import io.devarium.core.domain.user.exception.UserException;
import io.devarium.core.domain.user.port.out.UserRepository;
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
        if (user.getId() != null) {
            UserEntity entity = userJpaRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new UserException(
                    UserErrorCode.USER_EMAIL_NOT_FOUND,
                    user.getEmail())
                );
            entity.update(user);
            return userJpaRepository.save(entity).toDomain();
        }

        UserEntity entity = UserEntity.fromDomain(user);
        return userJpaRepository.save(entity).toDomain();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(UserEntity::toDomain);
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userJpaRepository.findById(userId).map(UserEntity::toDomain);
    }
}
