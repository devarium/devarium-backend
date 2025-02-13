package io.devarium.core.domain.membership.service;

import io.devarium.core.domain.membership.MemberRole;
import io.devarium.core.domain.membership.Membership;
import io.devarium.core.domain.membership.command.DeleteMemberships;
import io.devarium.core.domain.membership.command.UpdateMembership;
import io.devarium.core.domain.membership.command.UpdateMemberships;
import io.devarium.core.domain.membership.exception.MembershipErrorCode;
import io.devarium.core.domain.membership.exception.MembershipException;
import io.devarium.core.domain.membership.port.in.MembershipService;
import io.devarium.core.domain.membership.port.out.MembershipRepository;
import io.devarium.core.domain.user.User;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class MembershipServiceImpl implements MembershipService {

    private final MembershipRepository membershipRepository;

    @Override
    public void createMemberships(Long teamId, List<Long> userIds) {
        List<Membership> memberships = userIds.stream()
            .map(userId -> Membership.builder()
                .userId(userId)
                .teamId(teamId)
                .role(MemberRole.MEMBER)
                .build())
            .toList();

        membershipRepository.saveAll(memberships);
    }

    @Override
    public void createLeader(Long teamId, Long userId) {
        if (membershipRepository.existsByTeamId(teamId)) {
            throw new MembershipException(
                MembershipErrorCode.FIRST_MEMBERSHIP_ONLY,
                userId,
                teamId
            );
        }
        Membership membership = Membership.builder()
            .userId(userId)
            .teamId(teamId)
            .role(MemberRole.LEADER)
            .build();
        membershipRepository.save(membership);
    }

    @Override
    public Membership getMembership(Long teamId, Long userId) {
        return membershipRepository.findByUserIdAndTeamId(userId, teamId)
            .orElseThrow(() -> new MembershipException(
                MembershipErrorCode.MEMBERSHIP_NOT_IN_TEAM,
                userId,
                teamId
            ));
    }

    @Override
    public Page<Membership> getMemberships(Long teamId, User user, Pageable pageable) {
        getMembership(teamId, user.getId()).validateRole(MemberRole.MEMBER);

        return membershipRepository.findAllByTeamId(teamId, pageable);
    }

    @Override
    public List<Membership> getMemberships(Long userId) {
        return membershipRepository.findAllByUserId(userId);
    }

    @Override
    public List<Membership> getMemberships(Long teamId, Set<Long> userIds) {
        return membershipRepository.findAllByTeamIdAndUserIdIn(teamId, userIds);
    }

    @Override
    public List<Membership> updateMemberships(Long teamId, UpdateMemberships request, User user) {
        Membership leader = getMembership(teamId, user.getId());
        leader.validateRole(MemberRole.LEADER);
        Set<Long> ids = request.memberships().stream()
            .map(UpdateMembership::id)
            .collect(Collectors.toSet());
        if (ids.contains(leader.getId())) {
            throw new MembershipException(
                MembershipErrorCode.LEADER_ROLE_UNMODIFIABLE,
                leader.getId()
            );
        }

        Map<Long, Membership> membershipsMap = membershipRepository.findAllByIdInAndTeamId(
            ids,
            teamId
        ).stream().collect(Collectors.toMap(Membership::getId, membership -> membership));
        if (ids.size() != membershipsMap.size()) {
            List<Long> unmatchedIds = ids.stream()
                .filter(id -> !membershipsMap.containsKey(id)).toList();
            throw new MembershipException(
                MembershipErrorCode.MEMBERSHIPS_NOT_FOUND,
                unmatchedIds
            );
        }

        request.memberships().forEach(req -> updateMemberRole(membershipsMap, req));
        List<Membership> memberships = new LinkedList<>(membershipsMap.values());
        return membershipRepository.saveAll(memberships);
    }

    @Override
    public void updateLeader(Long teamId, Long oldLeaderId, Long newLeaderId) {
        Membership oldLeader = getMembership(teamId, oldLeaderId);
        Membership newLeader = getMembership(teamId, newLeaderId);

        oldLeader.update(MemberRole.MANAGER);
        newLeader.update(MemberRole.LEADER);
        membershipRepository.saveAll(List.of(oldLeader, newLeader));
    }

    @Override
    public void deleteMemberships(Long teamId, DeleteMemberships request, User user) {
        getMembership(teamId, user.getId()).validateRole(MemberRole.LEADER);
        membershipRepository.deleteAll(teamId, request.ids());
    }

    @Override
    public boolean checkMembershipExists(Long teamId, Long userId) {
        return membershipRepository.existsByTeamIdAndUserId(teamId, userId);
    }

    private void updateMemberRole(
        Map<Long, Membership> memberships,
        UpdateMembership updateRequest
    ) {
        Long id = updateRequest.id();
        MemberRole newRole = updateRequest.role();
        Membership membership = memberships.get(id);
        if (newRole == MemberRole.LEADER) {
            throw new MembershipException(MembershipErrorCode.INVALID_MEMBER_ROLE);
        }
        membership.update(newRole);
    }
}
