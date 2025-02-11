package io.devarium.core.domain.team.repository;

import io.devarium.core.domain.team.Team;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TeamRepository {

    Team save(Team team);

    Optional<Team> findById(Long id);

    List<Team> findAllById(List<Long> Ids);

    Page<Team> findByNameContaining(String name, Pageable pageable);

    boolean existsById(Long id);

    boolean existsByLeaderId(Long leaderId);
}
