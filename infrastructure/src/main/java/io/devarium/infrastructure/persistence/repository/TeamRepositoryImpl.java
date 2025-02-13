package io.devarium.infrastructure.persistence.repository;

import io.devarium.core.domain.team.Team;
import io.devarium.core.domain.team.exception.TeamErrorCode;
import io.devarium.core.domain.team.exception.TeamException;
import io.devarium.core.domain.team.port.out.TeamRepository;
import io.devarium.infrastructure.persistence.entity.TeamEntity;
import io.devarium.infrastructure.persistence.entity.UserEntity;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class TeamRepositoryImpl implements TeamRepository {

    private final EntityManager entityManager;
    private final TeamJpaRepository teamJpaRepository;

    @Override
    public Team save(Team team) {
        TeamEntity entity;
        UserEntity leader = entityManager.getReference(UserEntity.class, team.getLeaderId());
        if (team.getId() != null) {
            entity = teamJpaRepository.findById(team.getId())
                .orElseThrow(() -> new TeamException(TeamErrorCode.TEAM_NOT_FOUND, team.getId()));
            entity.update(team, leader);
        } else {
            entity = TeamEntity.fromDomain(team, leader);
        }
        return teamJpaRepository.save(entity).toDomain();
    }

    @Override
    public Optional<Team> findById(Long id) {
        return teamJpaRepository.findByIdAndDeletedAtIsNull(id).map(TeamEntity::toDomain);
    }

    @Override
    public List<Team> findAllById(List<Long> Ids) {
        return teamJpaRepository.findAllById(Ids).stream().map(TeamEntity::toDomain).toList();
    }

    @Override
    public Page<Team> findByNameContaining(String name, Pageable pageable) {
        return teamJpaRepository.findByNameContainingAndDeletedAtIsNull(name, pageable)
            .map(TeamEntity::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return teamJpaRepository.existsByIdAndDeletedAtIsNull(id);
    }

    @Override
    public boolean existsByLeaderId(Long leaderId) {
        return teamJpaRepository.existsByLeaderIdAndDeletedAtIsNull(leaderId);
    }
}
