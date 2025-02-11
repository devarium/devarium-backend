package io.devarium.infrastructure.persistence.repository;

import io.devarium.infrastructure.persistence.entity.MembershipEntity;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipJpaRepository extends JpaRepository<MembershipEntity, Long> {

    Optional<MembershipEntity> findByUserIdAndTeamId(
        Long userId,
        Long teamId
    );

    List<MembershipEntity> findAllByIdInAndTeamId(Set<Long> ids, Long teamId);

    Page<MembershipEntity> findAllByTeamIdAndUser_DeletedAtIsNull(
        Long teamId,
        Pageable pageable
    );

    List<MembershipEntity> findAllByUserIdAndTeam_DeletedAtIsNull(
        Long userId
    );

    List<MembershipEntity> findAllByTeamIdAndUserIdIn(Long teamId, Set<Long> userIds);

    boolean existsByTeamId(Long teamId);
}
