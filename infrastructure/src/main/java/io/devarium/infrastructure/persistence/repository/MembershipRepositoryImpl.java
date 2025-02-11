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
import java.util.Map;
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
        UserEntity user = entityManager.getReference(UserEntity.class, membership.getUserId());
        TeamEntity team = entityManager.getReference(TeamEntity.class, membership.getTeamId());
        MembershipEntity entity = MembershipEntity.fromDomain(membership, user, team);
        membershipJpaRepository.save(entity);
    }

    @Override
    public List<Membership> saveAll(List<Membership> memberships) {
        List<MembershipEntity> entities;

        if (memberships.getFirst().getId() != null) {
            entities = membershipJpaRepository.findAllById(
                memberships.stream().map(Membership::getId).toList()
            );
            if (entities.size() != memberships.size()) {
                throw new MembershipException(MembershipErrorCode.INVALID_UPDATE_ENTITY);
            }
            Map<Long, Membership> membershipMap = memberships.stream()
                .collect(Collectors.toMap(Membership::getId, membership -> membership));

            entities.forEach(entity -> {
                Membership updatedMembership = membershipMap.get(entity.getId());
                if (updatedMembership != null) {
                    entity.update(updatedMembership);
                }
            });
        } else {
            entities = memberships.stream()
                .map(membership -> MembershipEntity.fromDomain(
                    membership,
                    entityManager.getReference(UserEntity.class, membership.getUserId()),
                    entityManager.getReference(TeamEntity.class, membership.getTeamId())
                )).toList();
        }
        return membershipJpaRepository.saveAll(entities).stream()
            .map(MembershipEntity::toDomain).toList();
    }

    @Override
    public void deleteAll(Long teamId, Set<Long> ids) {
        List<MembershipEntity> entities = membershipJpaRepository.findAllByIdInAndTeamId(
            ids,
            teamId
        );
        if (entities.size() != ids.size()) {
            List<Long> entityIds = entities.stream().map(MembershipEntity::getId).toList();
            List<Long> unmatchedIds = ids.stream()
                .filter(id -> !entityIds.contains(id))
                .toList();
            throw new MembershipException(
                MembershipErrorCode.MEMBERSHIPS_NOT_FOUND,
                unmatchedIds
            );
        }
        membershipJpaRepository.deleteAll(entities);
    }

    @Override
    public Optional<Membership> findByUserIdAndTeamId(Long userId, Long teamId) {
        return membershipJpaRepository
            .findByUserIdAndTeamId(userId, teamId)
            .map(MembershipEntity::toDomain);
    }

    @Override
    public List<Membership> findAllByIdInAndTeamId(Set<Long> ids, Long teamId) {
        return membershipJpaRepository
            .findAllByIdInAndTeamIdAndTeam_DeletedAtIsNullAndUser_DeletedAtIsNull(
                ids,
                teamId
            ).stream().map(MembershipEntity::toDomain).toList();
    }

    @Override
    public Page<Membership> findAllByTeamId(Long teamId, Pageable pageable) {
        return membershipJpaRepository
            .findAllByTeamIdAndUser_DeletedAtIsNull(teamId, pageable)
            .map(MembershipEntity::toDomain);
    }

    @Override
    public List<Membership> findAllByUserId(Long userId) {
        return membershipJpaRepository
            .findAllByUserIdAndTeam_DeletedAtIsNull(userId).stream()
            .map(MembershipEntity::toDomain)
            .toList();
    }

    @Override
    public List<Membership> findAllByTeamIdAndUserIdIn(Long teamId, Set<Long> userIds) {
        return membershipJpaRepository.findAllByTeamIdAndUserIdIn(teamId, userIds).stream()
            .map(MembershipEntity::toDomain)
            .toList();
    }

    @Override
    public boolean existsByTeamId(Long teamId) {
        return membershipJpaRepository.existsByTeamId(teamId);
    }

    @Override
    public boolean existsByTeamIdAndUserId(Long teamId, Long userId) {
        return membershipJpaRepository.existsByTeamIdAndUserIdAndTeam_DeletedAtIsNull(teamId,
            userId);
    }
}
