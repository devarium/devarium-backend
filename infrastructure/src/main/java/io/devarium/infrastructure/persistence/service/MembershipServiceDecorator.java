package io.devarium.infrastructure.persistence.service;

import io.devarium.core.domain.membership.Membership;
import io.devarium.core.domain.membership.port.DeleteMemberships;
import io.devarium.core.domain.membership.port.UpdateMemberships;
import io.devarium.core.domain.membership.service.MembershipService;
import io.devarium.core.domain.membership.service.MembershipServiceImpl;
import io.devarium.core.domain.user.User;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class MembershipServiceDecorator implements MembershipService {

    private final MembershipServiceImpl memberService;

    @Override
    @Transactional
    public void createMemberships(Long teamId, Set<Long> userIds, User user) {
        memberService.createMemberships(teamId, userIds, user);
    }

    @Override
    @Transactional
    public void createLeader(Long teamId, Long userId) {
        memberService.createLeader(teamId, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Membership getMembership(Long teamId, Long userId) {
        return memberService.getMembership(teamId, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Membership> getMemberships(Pageable pageable, Long teamId, User user) {
        return memberService.getMemberships(pageable, teamId, user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Membership> getMemberships(Long userId) {
        return memberService.getMemberships(userId);
    }

    @Override
    @Transactional
    public void updateMemberships(Long teamId, UpdateMemberships request, User user) {
        memberService.updateMemberships(teamId, request, user);
    }

    @Override
    @Transactional
    public void updateLeader(Long teamId, Long oldLeaderId, Long newLeaderId) {
        memberService.updateLeader(teamId, oldLeaderId, newLeaderId);
    }

    @Override
    @Transactional
    public void deleteMemberships(Long teamId, DeleteMemberships request, User user) {
        memberService.deleteMemberships(teamId, request, user);
    }
}
