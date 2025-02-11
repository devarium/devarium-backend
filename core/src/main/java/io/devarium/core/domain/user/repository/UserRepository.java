package io.devarium.core.domain.user.repository;

import io.devarium.core.domain.user.User;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository {

    User save(User user);

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    List<User> findAllById(Set<Long> userIds);
}
