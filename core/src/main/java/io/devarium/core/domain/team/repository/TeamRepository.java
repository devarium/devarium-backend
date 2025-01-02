package io.devarium.core.domain.team.repository;

import io.devarium.core.domain.team.Team;

public interface TeamRepository {

    Team save(Team team);

    void deleteById(Long id);
}
