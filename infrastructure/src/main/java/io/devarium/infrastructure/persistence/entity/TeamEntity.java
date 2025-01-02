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
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
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
    private Set<UserEntity> members = new HashSet<>();

    private Instant deletedAt;

    @Builder
    private TeamEntity(
        String name,
        String description,
        String picture,
        String githubUrl,
        UserEntity leader,
        Set<UserEntity> members
    ) {
        this.name = name;
        this.description = description;
        this.picture = picture;
        this.githubUrl = githubUrl;
        this.leader = leader;
        this.members = members;
    }

    public static TeamEntity fromDomain(Team team, UserEntity leader, Set<UserEntity> members) {
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
                .collect(Collectors.toSet()))
            .deletedAt(deletedAt)
            .build();
    }

    public void update(Team domain) {
        this.name = domain.getName();
        this.description = domain.getDescription();
        this.picture = domain.getPicture();
        this.githubUrl = domain.getGithubUrl();
        this.deletedAt = domain.getDeletedAt();
    }

    public void updateLeader(UserEntity leader) {
        this.leader = leader;
    }

    public void updateMembers(Set<UserEntity> members) {
        this.members.clear();
        this.members.addAll(members);
    }

    // TODO : 상위 클래스에 두고 모든 엔티티가 일관적으로 동작하도록 수정
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TeamEntity team = (TeamEntity) o;
        return id != null && id.equals(team.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
