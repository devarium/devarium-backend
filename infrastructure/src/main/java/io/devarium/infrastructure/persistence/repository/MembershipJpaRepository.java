package io.devarium.infrastructure.persistence.repository;

import io.devarium.infrastructure.persistence.entity.MembershipEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipJpaRepository extends JpaRepository<MembershipEntity, Long> {

    Page<MembershipEntity> findByTeamId(Long teamId, Pageable pageable);

    List<MembershipEntity> findByUserId(Long userId);

    Optional<MembershipEntity> findByUserIdAndTeamId(Long userId, Long teamId);

    boolean existsByTeamId(Long teamId);
}
