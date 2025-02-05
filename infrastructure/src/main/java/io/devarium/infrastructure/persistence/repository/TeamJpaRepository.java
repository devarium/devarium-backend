package io.devarium.infrastructure.persistence.repository;

import io.devarium.infrastructure.persistence.entity.TeamEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamJpaRepository extends JpaRepository<TeamEntity, Long> {

    Page<TeamEntity> findByNameContaining(String name, Pageable pageable);
}
