package io.devarium.core.domain.user.repository;

import io.devarium.core.domain.user.User;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findByEmail(String email);
}
