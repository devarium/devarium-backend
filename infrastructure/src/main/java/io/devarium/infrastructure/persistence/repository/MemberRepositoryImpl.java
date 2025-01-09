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
        UserEntity user = entityManager.getReference(UserEntity.class, member.getUserId());
        TeamEntity team = entityManager.getReference(TeamEntity.class, member.getTeamId());

        MemberEntity entity = MemberEntity.fromDomain(member, user, team);
        memberJpaRepository.save(entity);
    }

    // TODO : team, user 삭제여부 확인 (deletedAt)
    // TODO : team, user 삭제 시 연관 members 삭제
    @Override
    public void saveAll(Long teamId, Set<Member> members) {
        TeamEntity team = entityManager.getReference(TeamEntity.class, teamId);
        Set<MemberEntity> entities = members.stream()
            .map(member -> {
                if (member.getId() != null) {
                    MemberEntity entity = memberJpaRepository.findById(member.getId())
                        .orElseThrow(() -> new MemberException(
                            MemberErrorCode.MEMBER_NOT_FOUND,
                            member.getId()
                        ));
                    entity.update(member.getRole(), member.isLeader());
                    return entity;
                }
                UserEntity user = entityManager.getReference(UserEntity.class, member.getUserId());
                return MemberEntity.fromDomain(member, user, team);
            })
            .collect(Collectors.toSet());
        memberJpaRepository.saveAll(entities);
    }

    @Override
    public void deleteAll(Set<Member> members) {
        Set<MemberEntity> entities = members.stream()
            .map(member -> memberJpaRepository.findById(member.getId())
                .orElseThrow(() -> new MemberException(
                    MemberErrorCode.MEMBER_NOT_FOUND,
                    member.getId()
                )))
            .collect(Collectors.toSet());
        memberJpaRepository.deleteAll(entities);
    }

    @Override
    public Optional<Member> findById(Long id) {
        return memberJpaRepository.findById(id).map(MemberEntity::toDomain);
    }

    @Override
    public Set<Member> findByIdIn(Set<Long> ids) {
        return memberJpaRepository.findByIdIn(ids).stream()
            .map(MemberEntity::toDomain)
            .collect(Collectors.toSet());
    }

    @Override
    public Page<Member> findByTeamId(Long teamId, Pageable pageable) {
        return memberJpaRepository.findByTeamId(teamId, pageable).map(MemberEntity::toDomain);
    }

    @Override
    public Page<Member> findByUserId(Long userId, Pageable pageable) {
        return memberJpaRepository.findByUserId(userId, pageable).map(MemberEntity::toDomain);
    }

    @Override
    public Optional<Member> findByUserIdAndTeamId(Long userId, Long teamId) {
        return memberJpaRepository.findByUserIdAndTeamId(userId, teamId)
            .map(MemberEntity::toDomain);
    }

    @Override
    public long countByTeamId(Long teamId) {
        return memberJpaRepository.countByTeamId(teamId);
    }
}
