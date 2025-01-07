package io.devarium.core.domain.team;

import io.devarium.core.domain.team.exception.TeamErrorCode;
import io.devarium.core.domain.team.exception.TeamException;
import java.time.Instant;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Team {

    public static final int DEFAULT_PAGE_SIZE = 1;

    private final Long id;

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
        Instant deletedAt
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.picture = picture;
        this.githubUrl = githubUrl;
        this.leaderId = leaderId;
        this.deletedAt = deletedAt;
    }

    public void validateLeader(Long userId) {
        if (!Objects.equals(this.leaderId, userId)) {
            throw new TeamException(TeamErrorCode.FORBIDDEN_ACCESS, userId, this.id);
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

    public void delete() {
        deletedAt = Instant.now();
    }
}
