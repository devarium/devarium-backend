package io.devarium.infrastructure.persistence.repository;

import io.devarium.infrastructure.persistence.entity.UserEntity;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    List<UserEntity> findAllByIdInAndDeletedAtIsNull(Set<Long> ids);
}
