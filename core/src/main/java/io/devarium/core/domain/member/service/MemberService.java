package io.devarium.core.domain.member.service;

import io.devarium.core.domain.member.Member;
import io.devarium.core.domain.member.port.CreateMembers;
import io.devarium.core.domain.member.port.DeleteMembers;
import io.devarium.core.domain.member.port.UpdateMembers;
import io.devarium.core.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberService {

    Page<Member> createMembers(Pageable pageable, Long teamId, CreateMembers request, User user);

    Page<Member> getMembersByTeamId(Pageable pageable, Long teamId, User user);

    Page<Member> getMembersByUser(Pageable pageable, User user);

    Page<Member> updateMembers(Pageable pageable, Long teamId, UpdateMembers request, User user);

    Page<Member> deleteMembers(Pageable pageable, Long teamId, DeleteMembers request, User user);
}
