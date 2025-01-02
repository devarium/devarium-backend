package io.devarium.core.domain.team;

import io.devarium.core.auth.exception.AuthErrorCode;
import io.devarium.core.auth.exception.CustomAuthException;
import java.time.Instant;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Team {

    private final Long id;
    private final Set<Long> memberIds;

    private String name;
    private String description;
    private String picture;
    private String githubUrl;
    private Long leaderId;
    private Instant deletedAt;

    @Builder
    public Team(
        Long id,
        String name,
        String description,
        String picture,
        String githubUrl,
        Long leaderId,
        Set<Long> memberIds,
        Instant deletedAt
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.picture = picture;
        this.githubUrl = githubUrl;
        this.leaderId = leaderId;
        this.memberIds = memberIds;
        this.deletedAt = deletedAt;
    }

    public void validateLeader(Long userId) {
        if (!this.leaderId.equals(userId)) {
            throw new CustomAuthException(AuthErrorCode.FORBIDDEN_ACCESS, userId, this.id);
        }
    }

    public void update(String name, String description, String picture, String githubUrl) {
        this.name = name;
        this.description = description;
        this.picture = picture;
        this.githubUrl = githubUrl;
    }

    public void updateLeader(Long newLeaderId) {
        this.leaderId = newLeaderId;
    }

    public void addMembers(Set<Long> memberIds) {
        this.memberIds.addAll(memberIds);
    }

    public void removeMembers(Set<Long> memberIds) {
        this.memberIds.removeAll(memberIds);
    }

    public void delete() {
        deletedAt = Instant.now();
    }
}
