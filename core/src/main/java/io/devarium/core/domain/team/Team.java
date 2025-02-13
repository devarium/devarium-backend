package io.devarium.core.domain.team;

import io.devarium.core.domain.team.exception.TeamErrorCode;
import io.devarium.core.domain.team.exception.TeamException;
import java.time.Instant;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Team {

    public static final int DEFAULT_PAGE_SIZE = 10;

    private final Long id;

    private String name;
    private String description;
    private String profileImageUrl;
    private String githubUrl;
    private Long leaderId;
    private Instant deletedAt;

    @Builder
    public Team(
        Long id,
        String name,
        String description,
        String profileImageUrl,
        String githubUrl,
        Long leaderId,
        Instant deletedAt
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.profileImageUrl = profileImageUrl;
        this.githubUrl = githubUrl;
        this.leaderId = leaderId;
        this.deletedAt = deletedAt;
    }

    public void validateLeader(Long userId) {
        if (!Objects.equals(this.leaderId, userId)) {
            throw new TeamException(TeamErrorCode.FORBIDDEN_ACCESS, userId, this.id);
        }
    }

    public void updateInfo(String description, String githubUrl) {
        this.description = description;
        this.githubUrl = githubUrl;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateProfileImage(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void updateLeader(Long newLeaderId) {
        this.leaderId = newLeaderId;
    }

    public void delete() {
        deletedAt = Instant.now();
    }
}
