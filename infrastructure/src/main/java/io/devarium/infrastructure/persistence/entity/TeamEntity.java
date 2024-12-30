package io.devarium.infrastructure.persistence.entity;

import io.devarium.core.domain.team.Team;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
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

    private String picture;

    private String githubUrl;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "leader_id", nullable = false)
    private UserEntity leader;

    @ManyToMany
    @JoinTable(
        name = "user_team",
        joinColumns = @JoinColumn(name = "team_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserEntity> members = new ArrayList<>();

    private Instant deletedAt;

    @Builder
    private TeamEntity(
        Long id,
        String name,
        String description,
        String picture,
        String githubUrl,
        UserEntity leader,
        List<UserEntity> members
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.picture = picture;
        this.githubUrl = githubUrl;
        this.leader = leader;
        this.members = members;
    }

    public static TeamEntity fromDomain(Team team, UserEntity leader, List<UserEntity> members) {
        return TeamEntity.builder()
            .name(team.getName())
            .description(team.getDescription())
            .picture(team.getPicture())
            .githubUrl(team.getGithubUrl())
            .leader(leader)
            .members(members)
            .build();
    }

    public Team toDomain() {
        return Team.builder()
            .id(id)
            .name(name)
            .description(description)
            .picture(picture)
            .githubUrl(githubUrl)
            .leaderId(leader.getId())
            .memberIds(members.stream()
                .map(UserEntity::getId)
                .toList())
            .deletedAt(deletedAt)
            .build();
    }

    public void update(Team domain) {
        this.name = domain.getName();
        this.description = domain.getDescription();
        this.picture = domain.getPicture();
        this.githubUrl = domain.getGithubUrl();
    }

    public void changeLeader(UserEntity leader) {
        this.leader = leader;
    }

    public void addMember(UserEntity user) {
        this.members.add(user);
    }

    public void removeMember(UserEntity user) {
        this.members.remove(user);
    }
}
