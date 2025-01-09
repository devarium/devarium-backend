package io.devarium.infrastructure.persistence.repository;

import io.devarium.core.domain.member.Member;
import io.devarium.core.domain.member.exception.MemberErrorCode;
import io.devarium.core.domain.member.exception.MemberException;
import io.devarium.core.domain.member.repository.MemberRepository;
import io.devarium.infrastructure.persistence.entity.MemberEntity;
import io.devarium.infrastructure.persistence.entity.TeamEntity;
import io.devarium.infrastructure.persistence.entity.UserEntity;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MemberRepositoryImpl implements MemberRepository {

    private final EntityManager entityManager;
    private final MemberJpaRepository memberJpaRepository;

    @Override
    public void save(Member member) {
        UserEntity user = checkUserDeleted(member.getUserId());
        TeamEntity team = checkTeamDeleted(member.getTeamId());
        MemberEntity entity = MemberEntity.fromDomain(member, user, team);
        memberJpaRepository.save(entity);
    }

    @Override
    public void saveAll(Long teamId, Set<Member> members) {
        TeamEntity team = checkTeamDeleted(teamId);
        Set<MemberEntity> entities = members.stream()
            .map(member -> {
                UserEntity user = checkUserDeleted(member.getUserId());
                if (member.getId() != null) {
                    MemberEntity entity = memberJpaRepository.findById(member.getId())
                        .orElseThrow(() -> new MemberException(
                            MemberErrorCode.MEMBER_NOT_FOUND,
                            member.getId()
                        ));
                    entity.update(member.getRole(), member.isLeader());
                    return entity;
                }
                return MemberEntity.fromDomain(member, user, team);
            })
            .collect(Collectors.toSet());
        memberJpaRepository.saveAll(entities);
    }

    @Override
    public void deleteAll(Set<Member> members) {
        Set<MemberEntity> entities = members.stream()
            .map(member -> {
                checkUserDeleted(member.getUserId());
                checkTeamDeleted(member.getTeamId());
                return memberJpaRepository.findById(member.getId())
                    .orElseThrow(() -> new MemberException(
                        MemberErrorCode.MEMBER_NOT_FOUND,
                        member.getId()
                    ));
            })
            .collect(Collectors.toSet());
        memberJpaRepository.deleteAll(entities);
    }

    @Override
    public Set<Member> findByIdIn(Set<Long> ids) {
        return memberJpaRepository.findByIdIn(ids).stream()
            .map(MemberEntity::toDomain)
            .collect(Collectors.toSet());
    }

    @Override
    public Page<Member> findByTeamId(Long teamId, Pageable pageable) {
        checkTeamDeleted(teamId);
        return memberJpaRepository.findByTeamId(teamId, pageable).map(MemberEntity::toDomain);
    }

    @Override
    public Page<Member> findByUserId(Long userId, Pageable pageable) {
        checkUserDeleted(userId);
        return memberJpaRepository.findByUserId(userId, pageable).map(MemberEntity::toDomain);
    }

    @Override
    public Optional<Member> findByUserIdAndTeamId(Long userId, Long teamId) {
        return memberJpaRepository.findByUserIdAndTeamId(userId, teamId)
            .map(MemberEntity::toDomain);
    }

    @Override
    public long countByTeamId(Long teamId) {
        checkTeamDeleted(teamId);
        return memberJpaRepository.countByTeamId(teamId);
    }

    private UserEntity checkUserDeleted(Long userId) {
        UserEntity user = entityManager.getReference(UserEntity.class, userId);
        if (user != null && user.getDeletedAt() != null) {
            throw new MemberException(MemberErrorCode.USER_DELETED, user.getId());
        }
        return user;
    }

    private TeamEntity checkTeamDeleted(Long teamId) {
        TeamEntity team = entityManager.getReference(TeamEntity.class, teamId);
        if (team != null && team.getDeletedAt() != null) {
            throw new MemberException(MemberErrorCode.TEAM_DELETED, team.getId());
        }
        return team;
    }
}
