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
    public void delete(Long id) {
        TeamEntity entity = teamJpaRepository.findById(id)
            .orElseThrow(() -> new TeamException(TeamErrorCode.TEAM_NOT_FOUND, id));
        teamJpaRepository.delete(entity);
    }

    @Override
    public Optional<Team> findById(Long id) {
        return teamJpaRepository.findById(id).map(TeamEntity::toDomain);
    }

    @Override
    public List<Team> findAllById(List<Long> Ids) {
        return teamJpaRepository.findAllById(Ids).stream().map(TeamEntity::toDomain).toList();
    }

    @Override
    public boolean existsByLeaderId(Long leaderId) {
        return teamJpaRepository.existsByLeaderId(leaderId);
    }

    @Override
    public Page<Team> findByNameContaining(String name, Pageable pageable) {
        return teamJpaRepository.findByNameContaining(name, pageable).map(TeamEntity::toDomain);
    }
}
