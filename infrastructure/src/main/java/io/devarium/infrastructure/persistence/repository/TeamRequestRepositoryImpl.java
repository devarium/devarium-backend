package io.devarium.infrastructure.persistence.repository;

import io.devarium.core.domain.teamRequest.TeamRequest;
import io.devarium.core.domain.teamRequest.TeamRequestStatus;
import io.devarium.core.domain.teamRequest.TeamRequestType;
import io.devarium.core.domain.teamRequest.exception.TeamRequestErrorCode;
import io.devarium.core.domain.teamRequest.exception.TeamRequestException;
import io.devarium.core.domain.teamRequest.repository.TeamRequestRepository;
import io.devarium.infrastructure.persistence.entity.TeamEntity;
import io.devarium.infrastructure.persistence.entity.TeamRequestEntity;
import io.devarium.infrastructure.persistence.entity.UserEntity;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class TeamRequestRepositoryImpl implements TeamRequestRepository {

    private final EntityManager entityManager;
    private final TeamRequestJpaRepository teamRequestJpaRepository;

    @Override
    public TeamRequest save(TeamRequest teamRequest) {
        TeamEntity team = entityManager.getReference(TeamEntity.class, teamRequest.getTeamId());
        UserEntity user = entityManager.getReference(UserEntity.class, teamRequest.getUserId());

        TeamRequestEntity entity = TeamRequestEntity.fromDomain(teamRequest, team, user);
        return teamRequestJpaRepository.save(entity).toDomain();
    }

    @Override
    public List<TeamRequest> saveAll(List<TeamRequest> teamRequests) {
        TeamEntity team = entityManager.getReference(
            TeamEntity.class,
            teamRequests.getFirst().getTeamId()
        );

        List<TeamRequestEntity> entities = teamRequests.stream()
            .map(request -> {
                UserEntity user = entityManager.getReference(UserEntity.class, request.getUserId());

                if (request.getId() != null) {
                    TeamRequestEntity entity = teamRequestJpaRepository.findById(request.getId())
                        .orElseThrow(() -> new TeamRequestException(
                            TeamRequestErrorCode.TEAM_REQUEST_NOT_FOUND, request.getId()
                        ));
                    entity.update(request);
                    return entity;
                }
                return TeamRequestEntity.fromDomain(request, team, user);
            })
            .toList();
        return teamRequestJpaRepository.saveAll(entities).stream()
            .map(TeamRequestEntity::toDomain).toList();
    }

    @Override
    public List<TeamRequest> findByTeamIdAndTypeAndStatus(
        Long teamId,
        TeamRequestType type,
        TeamRequestStatus status
    ) {
        return teamRequestJpaRepository.findByTeamIdAndTypeAndStatus(teamId, type, status).stream()
            .map(TeamRequestEntity::toDomain).toList();
    }

    @Override
    public List<TeamRequest> findByIdInAndTeamId(Long teamId, List<Long> ids) {
        return teamRequestJpaRepository.findByIdInAndTeamId(ids, teamId).stream()
            .map(TeamRequestEntity::toDomain).toList();
    }
}
