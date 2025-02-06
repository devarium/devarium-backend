package io.devarium.core.domain.member.service;

import io.devarium.core.domain.member.Member;
import io.devarium.core.domain.member.port.CreateMembers;
import io.devarium.core.domain.member.port.DeleteMembers;
import io.devarium.core.domain.member.port.UpdateMembers;
import io.devarium.core.domain.user.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberService {

    void createMembers(Long teamId, CreateMembers request, User user);

    void createLeader(Long teamId, Long userId);

    Page<Member> getMembers(Pageable pageable, Long teamId, User user);

    List<Member> getMembers(Long userId);

    void updateMembers(Long teamId, UpdateMembers request, User user);

    void updateLeader(Long teamId, Long oldLeaderId, Long newLeaderId);

    void deleteMembers(Long teamId, DeleteMembers request, User user);
}
