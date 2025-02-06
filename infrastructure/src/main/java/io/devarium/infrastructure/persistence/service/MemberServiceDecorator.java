package io.devarium.infrastructure.persistence.service;

import io.devarium.core.domain.member.Member;
import io.devarium.core.domain.member.port.DeleteMembers;
import io.devarium.core.domain.member.port.UpdateMembers;
import io.devarium.core.domain.member.service.MemberService;
import io.devarium.core.domain.member.service.MemberServiceImpl;
import io.devarium.core.domain.user.User;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class MemberServiceDecorator implements MemberService {

    private final MemberServiceImpl memberService;

    @Override
    @Transactional
    public void createMembers(Long teamId, Set<Long> userIds, User user) {
        memberService.createMembers(teamId, userIds, user);
    }

    @Override
    @Transactional
    public void createLeader(Long teamId, Long userId) {
        memberService.createLeader(teamId, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Member getMember(Long teamId, Long userId) {
        return memberService.getMember(teamId, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Member> getMembers(Pageable pageable, Long teamId, User user) {
        return memberService.getMembers(pageable, teamId, user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Member> getMembers(Long userId) {
        return memberService.getMembers(userId);
    }

    @Override
    @Transactional
    public void updateMembers(Long teamId, UpdateMembers request, User user) {
        memberService.updateMembers(teamId, request, user);
    }

    @Override
    @Transactional
    public void updateLeader(Long teamId, Long oldLeaderId, Long newLeaderId) {
        memberService.updateLeader(teamId, oldLeaderId, newLeaderId);
    }

    @Override
    @Transactional
    public void deleteMembers(Long teamId, DeleteMembers request, User user) {
        memberService.deleteMembers(teamId, request, user);
    }
}
