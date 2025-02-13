package io.devarium.infrastructure.persistence.repository;

import io.devarium.infrastructure.persistence.entity.TeamEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamJpaRepository extends JpaRepository<TeamEntity, Long> {

    Optional<TeamEntity> findByIdAndDeletedAtIsNull(Long id);

    Page<TeamEntity> findByNameContainingAndDeletedAtIsNull(String name, Pageable pageable);

    boolean existsByLeaderIdAndDeletedAtIsNull(Long leaderId);

    boolean existsByIdAndDeletedAtIsNull(Long id);
}
