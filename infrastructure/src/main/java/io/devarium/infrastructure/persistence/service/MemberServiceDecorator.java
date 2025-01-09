package io.devarium.infrastructure.persistence.service;

import io.devarium.core.domain.member.Member;
import io.devarium.core.domain.member.port.CreateMembers;
import io.devarium.core.domain.member.port.DeleteMembers;
import io.devarium.core.domain.member.port.UpdateMembers;
import io.devarium.core.domain.member.service.MemberService;
import io.devarium.core.domain.member.service.MemberServiceImpl;
import io.devarium.core.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class MemberServiceDecorator implements MemberService {

    private final MemberServiceImpl memberService;

    @Override
    @Transactional
    public void createMembers(Long teamId, CreateMembers request, User user) {
        memberService.createMembers(teamId, request, user);
    }

    @Override
    public void createFirstMember(Long teamId, Long userId) {
        memberService.createFirstMember(teamId, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Member> getMembersByTeamId(Pageable pageable, Long teamId, User user) {
        return memberService.getMembersByTeamId(pageable, teamId, user);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Member> getMembersByUser(Pageable pageable, User user) {
        return memberService.getMembersByUser(pageable, user);
    }

    @Override
    @Transactional
    public void updateMembers(Long teamId, UpdateMembers request, User user) {
        memberService.updateMembers(teamId, request, user);
    }

    @Override
    public void updateLeader(Long teamId, Long oldLeaderId, Long newLeaderId) {
        memberService.updateLeader(teamId, oldLeaderId, newLeaderId);
    }

    @Override
    @Transactional
    public void deleteMembers(Long teamId, DeleteMembers request, User user) {
        memberService.deleteMembers(teamId, request, user);
    }
}
