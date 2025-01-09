package io.devarium.core.domain.team.repository;

import io.devarium.core.domain.team.Team;
import java.util.Optional;

public interface TeamRepository {

    Team save(Team team);

    void delete(Long id);

    Optional<Team> findById(Long id);
}
