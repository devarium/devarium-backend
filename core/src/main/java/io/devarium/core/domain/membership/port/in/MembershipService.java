package io.devarium.core.domain.membership.port.in;

import io.devarium.core.domain.membership.Membership;
import io.devarium.core.domain.membership.command.DeleteMemberships;
import io.devarium.core.domain.membership.command.UpdateMemberships;
import io.devarium.core.domain.user.User;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MembershipService {

    void createMemberships(Long teamId, List<Long> userIds);

    void createLeader(Long teamId, Long userId);

    Membership getMembership(Long teamId, Long userId);

    Page<Membership> getMemberships(Long teamId, User user, Pageable pageable);

    List<Membership> getMemberships(Long userId);

    List<Membership> getMemberships(Long teamId, Set<Long> userIds);

    List<Membership> updateMemberships(Long teamId, UpdateMemberships request, User user);

    void updateLeader(Long teamId, Long oldLeaderId, Long newLeaderId);

    void deleteMemberships(Long teamId, DeleteMemberships request, User user);

    boolean checkMembershipExists(Long teamId, Long userId);
}
