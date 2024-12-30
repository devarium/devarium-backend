package io.devarium.core.domain.team;

import java.time.Instant;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Team {

    private final Long id;
    private final List<Long> memberIds;

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
        List<Long> memberIds,
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

    public void update(String name, String description, String picture, String githubUrl) {
        this.name = name;
        this.description = description;
        this.picture = picture;
        this.githubUrl = githubUrl;
    }

    public void changeLeader(Long newLeaderId) {
        this.leaderId = newLeaderId;
    }

    public void delete() {
        deletedAt = Instant.now();
    }
}
