package io.devarium.core.domain.membership.port.out;

import io.devarium.core.domain.membership.Membership;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MembershipRepository {

    void save(Membership membership);

    List<Membership> saveAll(List<Membership> memberships);

    void deleteAll(Long teamId, Set<Long> ids);

    Optional<Membership> findByUserIdAndTeamId(Long userId, Long teamId);

    List<Membership> findAllByIdInAndTeamId(Set<Long> ids, Long teamId);

    Page<Membership> findAllByTeamId(Long teamId, Pageable pageable);

    List<Membership> findAllByUserId(Long userId);

    List<Membership> findAllByTeamIdAndUserIdIn(Long teamId, Set<Long> userIds);

    boolean existsByTeamId(Long teamId);

    boolean existsByTeamIdAndUserId(Long teamId, Long userId);
}
