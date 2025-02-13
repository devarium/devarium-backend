package io.devarium.infrastructure.persistence.repository;

import io.devarium.core.domain.teamRequest.TeamRequest;
import io.devarium.core.domain.teamRequest.TeamRequestStatus;
import io.devarium.core.domain.teamRequest.TeamRequestType;
import io.devarium.core.domain.teamRequest.port.out.TeamRequestRepository;
import io.devarium.infrastructure.persistence.entity.TeamEntity;
import io.devarium.infrastructure.persistence.entity.TeamRequestEntity;
import io.devarium.infrastructure.persistence.entity.UserEntity;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
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
    public List<TeamRequest> saveAll(
        List<TeamRequest> createdTeamRequests,
        List<TeamRequest> updatedTeamRequests
    ) {
        List<TeamRequestEntity> entities = new ArrayList<>();

        if (createdTeamRequests != null && !createdTeamRequests.isEmpty()) {
            List<TeamRequestEntity> createdEntities = createdTeamRequests.stream()
                .map(teamRequest -> TeamRequestEntity.fromDomain(
                    teamRequest,
                    entityManager.getReference(TeamEntity.class, teamRequest.getTeamId()),
                    entityManager.getReference(UserEntity.class, teamRequest.getUserId())
                )).toList();
            entities.addAll(createdEntities);
        }

        if (updatedTeamRequests != null && !updatedTeamRequests.isEmpty()) {
            List<TeamRequestEntity> updatedEntities = teamRequestJpaRepository.findAllById(
                updatedTeamRequests.stream().map(TeamRequest::getId).toList()
            );
            Map<Long, TeamRequest> updatedTeamRequestMap = updatedTeamRequests.stream()
                .collect(Collectors.toMap(TeamRequest::getId, teamRequest -> teamRequest));

            updatedEntities.forEach(entity -> {
                TeamRequest updatedRequest = updatedTeamRequestMap.get(entity.getId());
                if (updatedRequest != null) {
                    entity.update(updatedRequest);
                }
            });
            entities.addAll(updatedEntities);
        }
        return teamRequestJpaRepository.saveAll(entities).stream()
            .map(TeamRequestEntity::toDomain).toList();
    }

    @Override
    public Optional<TeamRequest> findById(Long id) {
        return teamRequestJpaRepository.findByIdAndTeam_DeletedAtIsNull(id)
            .map(TeamRequestEntity::toDomain);
    }

    @Override
    public Optional<TeamRequest> findByTeamIdAndUserIdAndType(
        Long teamId,
        Long userId,
        TeamRequestType type
    ) {
        return teamRequestJpaRepository.findByTeamIdAndUserIdAndType(teamId, userId, type)
            .map(TeamRequestEntity::toDomain);
    }

    @Override
    public List<TeamRequest> findAllByTeamIdAndUserIdInAndType(
        Long teamId,
        Set<Long> userIds,
        TeamRequestType type
    ) {
        return teamRequestJpaRepository.findAllByTeamIdAndUserIdInAndType(teamId, userIds, type)
            .stream().map(TeamRequestEntity::toDomain).toList();
    }

    @Override
    public List<TeamRequest> findAllByTeamIdAndTypeAndStatusIn(
        Long teamId,
        TeamRequestType type,
        List<TeamRequestStatus> status
    ) {
        return teamRequestJpaRepository.findAllByTeamIdAndTypeAndStatusIn(teamId, type, status)
            .stream().map(TeamRequestEntity::toDomain).toList();
    }

    @Override
    public List<TeamRequest> findAllByUserIdAndTypeAndStatusIn(
        Long userId,
        TeamRequestType type,
        List<TeamRequestStatus> status
    ) {
        return teamRequestJpaRepository.findAllByUserIdAndTypeAndStatusIn(userId, type, status)
            .stream().map(TeamRequestEntity::toDomain).toList();
    }

    @Override
    public List<TeamRequest> findAllByIdInAndTeamId(Long teamId, Set<Long> ids) {
        return teamRequestJpaRepository.findAllByIdInAndTeamId(ids, teamId)
            .stream()
            .map(TeamRequestEntity::toDomain).toList();
    }
}
