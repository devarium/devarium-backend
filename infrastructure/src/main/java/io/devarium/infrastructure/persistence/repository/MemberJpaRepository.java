package io.devarium.infrastructure.persistence.repository;

import io.devarium.infrastructure.persistence.entity.MemberEntity;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<MemberEntity, Long> {

    Set<MemberEntity> findByIdIn(Set<Long> ids);

    Page<MemberEntity> findByTeamId(Long teamId, Pageable pageable);

    Page<MemberEntity> findByUserId(Long userId, Pageable pageable);

    Optional<MemberEntity> findByUserIdAndTeamId(Long userId, Long teamId);

    boolean existsByTeamId(Long teamId);
}
