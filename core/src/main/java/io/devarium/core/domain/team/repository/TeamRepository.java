package io.devarium.core.domain.team.repository;

import io.devarium.core.domain.team.Team;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TeamRepository {

    Team save(Team team);

    void delete(Long id);

    Optional<Team> findById(Long id);

    Page<Team> findAllByUserId(Long userId, Pageable pageable);
}
