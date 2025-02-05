package io.devarium.infrastructure.persistence.entity;

import io.devarium.core.domain.team.Team;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "teams")
public class TeamEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    private String profileImageUrl;

    private String githubUrl;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "leader_id", nullable = false)
    private UserEntity leader;

    private Instant deletedAt;

    @Builder
    private TeamEntity(
        String name,
        String description,
        String profileImageUrl,
        String githubUrl,
        UserEntity leader
    ) {
        this.name = name;
        this.description = description;
        this.profileImageUrl = profileImageUrl;
        this.githubUrl = githubUrl;
        this.leader = leader;
    }

    public static TeamEntity fromDomain(Team team, UserEntity leader) {
        return TeamEntity.builder()
            .name(team.getName())
            .description(team.getDescription())
            .profileImageUrl(team.getProfileImageUrl())
            .githubUrl(team.getGithubUrl())
            .leader(leader)
            .build();
    }

    public Team toDomain() {
        return Team.builder()
            .id(id)
            .name(name)
            .description(description)
            .profileImageUrl(profileImageUrl)
            .githubUrl(githubUrl)
            .leaderId(leader.getId())
            .deletedAt(deletedAt)
            .build();
    }

    public void update(Team domain, UserEntity leader) {
        this.name = domain.getName();
        this.description = domain.getDescription();
        this.profileImageUrl = domain.getProfileImageUrl();
        this.githubUrl = domain.getGithubUrl();
        this.leader = leader;
        this.deletedAt = domain.getDeletedAt();
    }
}
