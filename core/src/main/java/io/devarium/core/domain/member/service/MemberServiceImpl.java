package io.devarium.core.domain.member.service;

import io.devarium.core.domain.member.Member;
import io.devarium.core.domain.member.MemberRole;
import io.devarium.core.domain.member.exception.MemberErrorCode;
import io.devarium.core.domain.member.exception.MemberException;
import io.devarium.core.domain.member.port.CreateMembers;
import io.devarium.core.domain.member.port.DeleteMembers;
import io.devarium.core.domain.member.port.UpdateMember;
import io.devarium.core.domain.member.port.UpdateMembers;
import io.devarium.core.domain.member.repository.MemberRepository;
import io.devarium.core.domain.user.User;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public void createMembers(Long teamId, CreateMembers request, User user) {
        getUserMembership(teamId, user.getId())
            .validateRole(MemberRole.ADMIN);

        Set<Member> members = request.members().stream()
            .map(member -> {
                Long userId = member.userId();
                MemberRole role = member.role();

                if (role == MemberRole.SUPER_ADMIN) {
                    throw new MemberException(
                        MemberErrorCode.FIRST_MEMBER_ONLY,
                        userId,
                        teamId
                    );
                }
                return Member.builder()
                    .userId(userId)
                    .teamId(teamId)
                    .role(role)
                    .isLeader(false)
                    .build();
            })
            .collect(Collectors.toSet());

        memberRepository.saveAll(teamId, members);
    }

    @Override
    public void createLeader(Long teamId, Long userId) {
        if (memberRepository.countByTeamId(teamId) != 0) {
            throw new MemberException(MemberErrorCode.FIRST_MEMBER_ONLY, userId, teamId);
        }
        Member member = Member.builder()
            .userId(userId)
            .teamId(teamId)
            .role(MemberRole.SUPER_ADMIN)
            .isLeader(true)
            .build();
        memberRepository.save(member);
    }

    @Override
    public Page<Member> getMembersByTeamId(Pageable pageable, Long teamId, User user) {
        getUserMembership(teamId, user.getId())
            .validateRole(MemberRole.VIEWER);

        return memberRepository.findByTeamId(teamId, pageable);
    }

    @Override
    public Page<Member> getMembersByUser(Pageable pageable, User user) {
        return memberRepository.findByUserId(user.getId(), pageable);
    }

    @Override
    public void updateMembers(Long teamId, UpdateMembers request, User user) {
        Member userMember = getUserMembership(teamId, user.getId());
        userMember.validateRole(MemberRole.ADMIN);

        Set<Long> memberIds = request.members().stream()
            .map(UpdateMember::memberId)
            .collect(Collectors.toSet());
        Map<Long, Member> members = memberRepository.findByIdIn(memberIds).stream()
            .collect(Collectors.toMap(Member::getId, Function.identity()));

        request.members()
            .forEach(updateRequest -> updateMemberRole(teamId, userMember, members, updateRequest));
        memberRepository.saveAll(teamId, new HashSet<>(members.values()));
    }

    @Override
    public void updateLeader(Long teamId, Long oldLeaderId, Long newLeaderId) {
        Member oldLeader = getUserMembership(teamId, oldLeaderId);
        if (!oldLeader.isLeader()) {
            throw new MemberException(
                MemberErrorCode.FORBIDDEN_ACCESS,
                oldLeader.getId(),
                oldLeader.getUserId(),
                teamId
            );
        }
        oldLeader.update(MemberRole.ADMIN);
        Member newLeader = getUserMembership(teamId, newLeaderId);
        newLeader.update(MemberRole.SUPER_ADMIN);
        memberRepository.saveAll(teamId, Set.of(oldLeader, newLeader));
    }

    @Override
    public void deleteMembers(Long teamId, DeleteMembers request, User user) {
        getUserMembership(teamId, user.getId())
            .validateRole(MemberRole.ADMIN);

        Set<Member> members = memberRepository.findByIdIn(request.memberIds());
        members.forEach(member -> member.validateMembership(teamId));
        memberRepository.deleteAll(members);
    }

    private Member getUserMembership(Long teamId, Long userId) {
        return memberRepository.findByUserIdAndTeamId(userId, teamId)
            .orElseThrow(() -> new MemberException(
                MemberErrorCode.MEMBER_NOT_IN_TEAM,
                userId,
                teamId
            ));
    }

    private void updateMemberRole(
        Long teamId,
        Member userMember,
        Map<Long, Member> members,
        UpdateMember updateRequest
    ) {
        Long memberId = updateRequest.memberId();
        MemberRole newRole = updateRequest.role();

        Member member = members.get(memberId);
        if (member == null) {
            throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND, memberId);
        }

        member.validateMembership(teamId);
        validateRoleChange(userMember, member, newRole);

        member.update(newRole);
    }

    private void validateRoleChange(
        Member userMember,
        Member member,
        MemberRole newRole
    ) {
        MemberRole oldRole = member.getRole();

        if (userMember.getRole().getLevel() <= oldRole.getLevel()) {
            throw new MemberException(MemberErrorCode.MEMBER_ROLE_HIERARCHY_VIOLATION);
        }

        if (newRole == MemberRole.SUPER_ADMIN) {
            throw new MemberException(MemberErrorCode.UPDATE_LEADER_ONLY, member.getId());
        }
    }
}
