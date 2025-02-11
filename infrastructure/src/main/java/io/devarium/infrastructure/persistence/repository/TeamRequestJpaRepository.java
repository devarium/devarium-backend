package io.devarium.infrastructure.persistence.repository;

import io.devarium.core.domain.teamRequest.TeamRequestStatus;
import io.devarium.core.domain.teamRequest.TeamRequestType;
import io.devarium.infrastructure.persistence.entity.TeamRequestEntity;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRequestJpaRepository extends JpaRepository<TeamRequestEntity, Long> {

    Optional<TeamRequestEntity> findByIdAndTeam_DeletedAtIsNull(Long id);

    Optional<TeamRequestEntity> findByTeamIdAndUserIdAndType(
        Long teamId,
        Long UserId,
        TeamRequestType type
    );

    List<TeamRequestEntity> findAllByTeamIdAndUserIdInAndType(
        Long teamId,
        Set<Long> userIds,
        TeamRequestType type
    );

    List<TeamRequestEntity> findAllByTeamIdAndTypeAndStatusIn(
        Long teamId,
        TeamRequestType type,
        List<TeamRequestStatus> status
    );

    List<TeamRequestEntity> findAllByUserIdAndTypeAndStatusIn(
        Long userId,
        TeamRequestType type,
        List<TeamRequestStatus> status
    );

    List<TeamRequestEntity> findAllByIdInAndTeamId(Set<Long> ids, Long teamId);
}
