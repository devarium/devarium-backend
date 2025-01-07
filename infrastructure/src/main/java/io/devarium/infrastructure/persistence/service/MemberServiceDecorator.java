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
    public Page<Member> createMembers(Pageable pageable, Long teamId, CreateMembers request,
        User user) {
        return memberService.createMembers(pageable, teamId, request, user);
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
    public Page<Member> updateMembers(Pageable pageable, Long teamId, UpdateMembers request,
        User user) {
        return memberService.updateMembers(pageable, teamId, request, user);
    }

    @Override
    @Transactional
    public Page<Member> deleteMembers(Pageable pageable, Long teamId, DeleteMembers request,
        User user) {
        return memberService.deleteMembers(pageable, teamId, request, user);
    }
}
