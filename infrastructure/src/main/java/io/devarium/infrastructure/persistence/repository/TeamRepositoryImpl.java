package io.devarium.infrastructure.persistence.repository;

import io.devarium.core.domain.team.Team;
import io.devarium.core.domain.team.exception.TeamErrorCode;
import io.devarium.core.domain.team.exception.TeamException;
import io.devarium.core.domain.team.repository.TeamRepository;
import io.devarium.infrastructure.persistence.entity.TeamEntity;
import io.devarium.infrastructure.persistence.entity.UserEntity;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class TeamRepositoryImpl implements TeamRepository {

    private final EntityManager entityManager;
    private final TeamJpaRepository teamJpaRepository;

    @Override
    public Team save(Team team) {
        UserEntity leader = entityManager.getReference(UserEntity.class, team.getLeaderId());
        Set<UserEntity> members = team.getMemberIds().stream()
            .map(memberId -> entityManager.getReference(UserEntity.class, memberId))
            .collect(Collectors.toSet());

        if (team.getId() != null) {
            TeamEntity entity = teamJpaRepository.findById(team.getId())
                .orElseThrow(() -> new TeamException(TeamErrorCode.TEAM_NOT_FOUND, team.getId()));
            entity.update(team);
            entity.updateLeader(leader);
            entity.updateMembers(members);
            return teamJpaRepository.save(entity).toDomain();
        }

        TeamEntity entity = TeamEntity.fromDomain(team, leader, members);
        return teamJpaRepository.save(entity).toDomain();
    }

    @Override
    public void delete(Long id) {
        TeamEntity entity = teamJpaRepository.findById(id)
            .orElseThrow(() -> new TeamException(TeamErrorCode.TEAM_NOT_FOUND, id));
        entity.getMembers().clear();
        teamJpaRepository.delete(entity);
    }

    @Override
    public Optional<Team> findById(Long id) {
        return teamJpaRepository.findById(id).map(TeamEntity::toDomain);
    }
}
