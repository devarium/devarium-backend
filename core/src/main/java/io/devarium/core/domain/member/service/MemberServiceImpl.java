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

        Set<Member> members = request.userIdToRole()
            .entrySet().stream()
            .map(member -> Member.builder()
                .userId(member.getKey())
                .teamId(teamId)
                .role(member.getValue())
                .build())
            .collect(Collectors.toSet());
        memberRepository.saveAll(teamId, members);
    }

    @Override
    public void createFirstMember(Long teamId, Long userId) {
        if (memberRepository.countByTeamId(teamId) != 0) {
            throw new MemberException(MemberErrorCode.FIRST_MEMBER_ONLY);
        }
        Member member = Member.builder()
            .userId(userId)
            .teamId(teamId)
            .role(MemberRole.SUPER_ADMIN)
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
        getUserMembership(teamId, user.getId())
            .validateRole(MemberRole.ADMIN);

        Set<Long> memberIds = new HashSet<>(request.memberIdToRole().keySet());
        Set<Member> members = memberRepository.findByIdIn(memberIds);
        members.forEach(member -> {
            member.validateMembership(teamId);
            MemberRole newRole = request.memberIdToRole().get(member.getId());
            member.update(newRole);
        });
        memberRepository.saveAll(teamId, members);
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
