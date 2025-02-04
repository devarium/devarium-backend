package io.devarium.infrastructure.persistence.repository;

import io.devarium.core.domain.teamRequest.TeamRequestStatus;
import io.devarium.core.domain.teamRequest.TeamRequestType;
import io.devarium.infrastructure.persistence.entity.TeamRequestEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRequestJpaRepository extends JpaRepository<TeamRequestEntity, Long> {

    List<TeamRequestEntity> findByTeamIdAndTypeAndStatus(
        Long teamId,
        TeamRequestType type,
        TeamRequestStatus status
    );

    List<TeamRequestEntity> findByIdInAndTeamId(List<Long> ids, Long teamId);
}
