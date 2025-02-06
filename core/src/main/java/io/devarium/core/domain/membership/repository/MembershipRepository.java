package io.devarium.core.domain.membership.repository;

import io.devarium.core.domain.membership.Membership;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MembershipRepository {

    void save(Membership membership);

    void saveAll(Long teamId, Set<Membership> memberships);

    void deleteAll(Set<Membership> memberships);

    Set<Membership> findAllById(Set<Long> ids);

    Page<Membership> findByTeamId(Long teamId, Pageable pageable);

    List<Membership> findByUserId(Long userId);

    Optional<Membership> findByUserIdAndTeamId(Long userId, Long teamId);

    boolean existsByTeamId(Long teamId);
}
