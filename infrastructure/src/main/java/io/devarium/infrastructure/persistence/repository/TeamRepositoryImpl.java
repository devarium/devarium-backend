package io.devarium.infrastructure.persistence.repository;

import io.devarium.core.domain.team.Team;
import io.devarium.core.domain.team.exception.TeamErrorCode;
import io.devarium.core.domain.team.exception.TeamException;
import io.devarium.core.domain.team.repository.TeamRepository;
import io.devarium.infrastructure.persistence.entity.TeamEntity;
import io.devarium.infrastructure.persistence.entity.UserEntity;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class TeamRepositoryImpl implements TeamRepository {

    private final EntityManager entityManager;
    private final TeamJpaRepository teamJpaRepository;

    @Override
    public Team save(Team team) {
        if (team.getId() != null) {
            TeamEntity entity = teamJpaRepository.findById(team.getId())
                .orElseThrow(() -> new TeamException(TeamErrorCode.TEAM_NOT_FOUND, team.getId()));
            entity.update(team);
            return teamJpaRepository.save(entity).toDomain();
        }

        UserEntity leader = entityManager.getReference(UserEntity.class, team.getLeaderId());
        List<UserEntity> members = team.getMemberIds().stream()
            .map(memberId -> entityManager.getReference(UserEntity.class, memberId))
            .toList();

        TeamEntity entity = TeamEntity.fromDomain(team, leader, members);
        return teamJpaRepository.save(entity).toDomain();
    }

    @Override
    public void deleteById(Long id) {
        teamJpaRepository.findById(id);
    }

    @Override
    public Optional<Team> findById(Long id) {
        return teamJpaRepository.findById(id).map(TeamEntity::toDomain);
    }
}
