package io.devarium.infrastructure.persistence.repository;

import io.devarium.infrastructure.persistence.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamJpaRepository extends JpaRepository<TeamEntity, Long> {

}
