package io.devarium.core.domain.member.service;

import io.devarium.core.domain.member.Member;
import io.devarium.core.domain.member.MemberRole;
import io.devarium.core.domain.member.exception.MemberErrorCode;
import io.devarium.core.domain.member.exception.MemberException;
import io.devarium.core.domain.member.port.CreateMembers;
import io.devarium.core.domain.member.port.DeleteMembers;
import io.devarium.core.domain.member.port.UpdateMembers;
import io.devarium.core.domain.member.repository.MemberRepository;
import io.devarium.core.domain.user.User;
import java.util.HashSet;
import java.util.Set;
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
    public void createFirstMember(Long teamId, Long userId) {
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

        Set<Long> memberIds = new HashSet<>(request.memberIdToRole().keySet());
        Set<Member> members = memberRepository.findByIdIn(memberIds);
        members.forEach(member -> {
            member.validateMembership(teamId);
            MemberRole oldRole = member.getRole();
            if (userMember.getRole().getLevel() <= oldRole.getLevel()) {
                throw new MemberException(MemberErrorCode.MEMBER_ROLE_HIERARCHY_VIOLATION);
            }

            MemberRole newRole = request.memberIdToRole().get(member.getId());
            if (newRole == MemberRole.SUPER_ADMIN) {
                throw new MemberException(MemberErrorCode.UPDATE_LEADER_ONLY, member.getId());
            }
            member.update(newRole);
        });
        memberRepository.saveAll(teamId, members);
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
}
