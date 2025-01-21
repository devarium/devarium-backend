package io.devarium.infrastructure.persistence.repository;

import io.devarium.infrastructure.persistence.entity.TeamEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeamJpaRepository extends JpaRepository<TeamEntity, Long> {

    @Query("SELECT t FROM MemberEntity m JOIN m.team t WHERE m.user.id = :userId ORDER BY m.createdAt DESC")
    Page<TeamEntity> findAllByUserId(@Param("userId") Long userId, Pageable pageable);
}
