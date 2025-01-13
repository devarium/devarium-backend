package io.devarium.core.domain.member.repository;

import io.devarium.core.domain.member.Member;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepository {

    void save(Member member);

    void saveAll(Long teamId, Set<Member> members);

    void deleteAll(Set<Member> members);

    Set<Member> findByIdIn(Set<Long> ids);

    Page<Member> findByTeamId(Long teamId, Pageable pageable);

    Page<Member> findByUserId(Long userId, Pageable pageable);

    Optional<Member> findByUserIdAndTeamId(Long userId, Long teamId);

    boolean existsByTeamId(Long teamId);
}
