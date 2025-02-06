package io.devarium.core.domain.membership.service;

import io.devarium.core.domain.membership.MemberRole;
import io.devarium.core.domain.membership.Membership;
import io.devarium.core.domain.membership.exception.MembershipErrorCode;
import io.devarium.core.domain.membership.exception.MembershipException;
import io.devarium.core.domain.membership.port.DeleteMemberships;
import io.devarium.core.domain.membership.port.UpdateMembership;
import io.devarium.core.domain.membership.port.UpdateMemberships;
import io.devarium.core.domain.membership.repository.MembershipRepository;
import io.devarium.core.domain.user.User;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class MembershipServiceImpl implements MembershipService {

    private final MembershipRepository membershipRepository;

    @Override
    public void createMemberships(Long teamId, Set<Long> userIds, User user) {
        getMembership(teamId, user.getId()).validateRole(MemberRole.MANAGER);

        Set<Membership> memberships = userIds.stream()
            .map(userId -> Membership.builder()
                .userId(userId)
                .teamId(teamId)
                .role(MemberRole.MEMBER)
                .isLeader(false)
                .build())
            .collect(Collectors.toSet());

        membershipRepository.saveAll(teamId, memberships);
    }

    @Override
    public void createLeader(Long teamId, Long userId) {
        if (!membershipRepository.existsByTeamId(teamId)) {
            throw new MembershipException(MembershipErrorCode.FIRST_MEMBERSHIP_ONLY, userId,
                teamId);
        }
        Membership membership = Membership.builder()
            .userId(userId)
            .teamId(teamId)
            .role(MemberRole.LEADER)
            .isLeader(true)
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
    public Page<Membership> getMemberships(Pageable pageable, Long teamId, User user) {
        getMembership(teamId, user.getId()).validateRole(MemberRole.MEMBER);

        return membershipRepository.findByTeamId(teamId, pageable);
    }

    @Override
    public List<Membership> getMemberships(Long userId) {
        return membershipRepository.findByUserId(userId);
    }

    @Override
    public void updateMemberships(Long teamId, UpdateMemberships request, User user) {
        Membership userMembership = getMembership(teamId, user.getId());
        userMembership.validateRole(MemberRole.MANAGER);

        Set<Long> membershipIds = request.memberships().stream()
            .map(UpdateMembership::membershipId)
            .collect(Collectors.toSet());
        Map<Long, Membership> memberships = membershipRepository.findAllById(membershipIds).stream()
            .collect(Collectors.toMap(Membership::getId, Function.identity()));

        request.memberships()
            .forEach(updateRequest -> updateMemberRole(teamId, userMembership, memberships,
                updateRequest));
        membershipRepository.saveAll(teamId, new HashSet<>(memberships.values()));
    }

    @Override
    public void updateLeader(Long teamId, Long oldLeaderId, Long newLeaderId) {
        Membership oldLeader = getMembership(teamId, oldLeaderId);
        Membership newLeader = getMembership(teamId, newLeaderId);

        oldLeader.update(MemberRole.MANAGER);
        newLeader.update(MemberRole.LEADER);
        membershipRepository.saveAll(teamId, Set.of(oldLeader, newLeader));
    }

    @Override
    public void deleteMemberships(Long teamId, DeleteMemberships request, User user) {
        getMembership(teamId, user.getId())
            .validateRole(MemberRole.MANAGER);

        Set<Membership> memberships = membershipRepository.findAllById(request.ids());
        memberships.forEach(membership -> membership.validateTeam(teamId));
        membershipRepository.deleteAll(memberships);
    }

    private void updateMemberRole(
        Long teamId,
        Membership userMembership,
        Map<Long, Membership> memberships,
        UpdateMembership updateRequest
    ) {
        Long membershipId = updateRequest.membershipId();
        MemberRole newRole = updateRequest.role();

        Membership membership = memberships.get(membershipId);
        if (membership == null) {
            throw new MembershipException(MembershipErrorCode.MEMBERSHIP_NOT_FOUND, membershipId);
        }

        membership.validateTeam(teamId);
        validateRoleChange(userMembership, membership, newRole);

        membership.update(newRole);
    }

    private void validateRoleChange(
        Membership userMembership,
        Membership membership,
        MemberRole newRole
    ) {
        MemberRole oldRole = membership.getRole();

        if (userMembership.getRole().getLevel() <= oldRole.getLevel()) {
            throw new MembershipException(MembershipErrorCode.MEMBER_ROLE_HIERARCHY_VIOLATION);
        }

        if (newRole == MemberRole.LEADER) {
            throw new MembershipException(MembershipErrorCode.UPDATE_LEADER_ONLY,
                membership.getId());
        }
    }
}
