package io.devarium.infrastructure.persistence.repository;

import io.devarium.core.domain.membership.Membership;
import io.devarium.core.domain.membership.exception.MembershipErrorCode;
import io.devarium.core.domain.membership.exception.MembershipException;
import io.devarium.core.domain.membership.repository.MembershipRepository;
import io.devarium.infrastructure.persistence.entity.MembershipEntity;
import io.devarium.infrastructure.persistence.entity.TeamEntity;
import io.devarium.infrastructure.persistence.entity.UserEntity;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MembershipRepositoryImpl implements MembershipRepository {

    private final EntityManager entityManager;
    private final MembershipJpaRepository membershipJpaRepository;

    @Override
    public void save(Membership membership) {
        UserEntity user = checkUserDeleted(membership.getUserId());
        TeamEntity team = checkTeamDeleted(membership.getTeamId());
        MembershipEntity entity = MembershipEntity.fromDomain(membership, user, team);
        membershipJpaRepository.save(entity);
    }

    @Override
    public void saveAll(Long teamId, Set<Membership> memberships) {
        TeamEntity team = checkTeamDeleted(teamId);
        Set<MembershipEntity> entities = memberships.stream()
            .map(membership -> {
                UserEntity user = checkUserDeleted(membership.getUserId());
                if (membership.getId() != null) {
                    MembershipEntity entity = membershipJpaRepository.findById(membership.getId())
                        .orElseThrow(() -> new MembershipException(
                            MembershipErrorCode.MEMBERSHIP_NOT_FOUND,
                            membership.getId()
                        ));
                    entity.update(membership.getRole(), membership.isLeader());
                    return entity;
                }
                return MembershipEntity.fromDomain(membership, user, team);
            })
            .collect(Collectors.toSet());
        membershipJpaRepository.saveAll(entities);
    }

    @Override
    public void deleteAll(Set<Membership> memberships) {
        Set<MembershipEntity> entities = memberships.stream()
            .map(membership -> {
                checkUserDeleted(membership.getUserId());
                checkTeamDeleted(membership.getTeamId());
                return membershipJpaRepository.findById(membership.getId())
                    .orElseThrow(() -> new MembershipException(
                        MembershipErrorCode.MEMBERSHIP_NOT_FOUND,
                        membership.getId()
                    ));
            })
            .collect(Collectors.toSet());
        membershipJpaRepository.deleteAll(entities);
    }

    @Override
    public Set<Membership> findAllById(Set<Long> ids) {
        return membershipJpaRepository.findAllById(ids).stream()
            .map(MembershipEntity::toDomain)
            .collect(Collectors.toSet());
    }

    @Override
    public Page<Membership> findByTeamId(Long teamId, Pageable pageable) {
        checkTeamDeleted(teamId);
        return membershipJpaRepository.findByTeamId(teamId, pageable)
            .map(MembershipEntity::toDomain);
    }

    @Override
    public List<Membership> findByUserId(Long userId) {
        checkUserDeleted(userId);
        return membershipJpaRepository.findByUserId(userId).stream()
            .map(MembershipEntity::toDomain)
            .toList();
    }

    @Override
    public Optional<Membership> findByUserIdAndTeamId(Long userId, Long teamId) {
        return membershipJpaRepository.findByUserIdAndTeamId(userId, teamId)
            .map(MembershipEntity::toDomain);
    }

    @Override
    public boolean existsByTeamId(Long teamId) {
        checkTeamDeleted(teamId);
        return membershipJpaRepository.existsByTeamId(teamId);
    }

    private UserEntity checkUserDeleted(Long userId) {
        UserEntity user = entityManager.getReference(UserEntity.class, userId);
        if (user != null && user.getDeletedAt() != null) {
            throw new MembershipException(MembershipErrorCode.USER_DELETED, user.getId());
        }
        return user;
    }

    private TeamEntity checkTeamDeleted(Long teamId) {
        TeamEntity team = entityManager.getReference(TeamEntity.class, teamId);
        if (team != null && team.getDeletedAt() != null) {
            throw new MembershipException(MembershipErrorCode.TEAM_DELETED, team.getId());
        }
        return team;
    }
}
