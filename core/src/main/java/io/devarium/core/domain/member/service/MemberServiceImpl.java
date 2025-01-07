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
    public Page<Member> createMembers(
        Pageable pageable,
        Long teamId,
        CreateMembers request,
        User user
    ) {
        getUserMembership(teamId, user.getId()).validateRole(MemberRole.ADMIN);
        Set<Member> members = request.userIdToRole()
            .entrySet().stream()
            .map(member -> Member.builder()
                .userId(member.getKey())
                .teamId(teamId)
                .role(member.getValue())
                .build())
            .collect(Collectors.toSet());
        memberRepository.saveAll(teamId, members);
        return memberRepository.findByTeamId(teamId, pageable);
    }

    @Override
    public Page<Member> getMembersByTeamId(Pageable pageable, Long teamId, User user) {
        getUserMembership(teamId, user.getId());
        return memberRepository.findByTeamId(teamId, pageable);
    }

    @Override
    public Page<Member> getMembersByUser(Pageable pageable, User user) {
        return memberRepository.findByUserId(user.getId(), pageable);
    }

    @Override
    public Page<Member> updateMembers(
        Pageable pageable,
        Long teamId,
        UpdateMembers request,
        User user
    ) {
        getUserMembership(teamId, user.getId()).validateRole(MemberRole.ADMIN);
        Set<Long> memberIds = new HashSet<>(request.memberIdToRole().keySet());
        Set<Member> members = memberRepository.findByIdIn(memberIds);
        members.forEach(member -> {
            member.validateMember(teamId);
            MemberRole newRole = request.memberIdToRole().get(member.getId());
            member.update(newRole);
        });
        memberRepository.saveAll(teamId, members);
        return memberRepository.findByTeamId(teamId, pageable);
    }

    @Override
    public Page<Member> deleteMembers(
        Pageable pageable,
        Long teamId,
        DeleteMembers request,
        User user
    ) {
        getUserMembership(teamId, user.getId()).validateRole(MemberRole.ADMIN);
        Set<Member> members = memberRepository.findByIdIn(request.memberIds());
        members.forEach(member -> member.validateMember(teamId));
        memberRepository.deleteAll(members);
        return memberRepository.findByTeamId(teamId, pageable);
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
