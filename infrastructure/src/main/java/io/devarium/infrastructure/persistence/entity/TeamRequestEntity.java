package io.devarium.infrastructure.persistence.entity;

import io.devarium.core.domain.teamRequest.TeamRequest;
import io.devarium.core.domain.teamRequest.TeamRequestStatus;
import io.devarium.core.domain.teamRequest.TeamRequestType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    name = "team_requests",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"team_id", "user_id", "type"}
    )
)
public class TeamRequestEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private TeamEntity team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TeamRequestType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TeamRequestStatus status;

    private Instant statusChangedAt;

    @Builder
    private TeamRequestEntity(
        TeamEntity team,
        UserEntity user,
        TeamRequestType type,
        TeamRequestStatus status
    ) {
        this.team = team;
        this.user = user;
        this.type = type;
        this.status = status;
        this.statusChangedAt = Instant.now();
    }

    public static TeamRequestEntity fromDomain(TeamRequest teamRequest, TeamEntity team,
        UserEntity user) {
        return TeamRequestEntity.builder()
            .team(team)
            .user(user)
            .type(teamRequest.getType())
            .status(teamRequest.getStatus())
            .build();
    }

    public TeamRequest toDomain() {
        return TeamRequest.builder()
            .id(id)
            .teamId(team.getId())
            .userId(user.getId())
            .type(type)
            .status(status)
            .statusChangedAt(statusChangedAt)
            .build();
    }

    public void update(TeamRequest domain) {
        this.status = domain.getStatus();
        this.statusChangedAt = domain.getStatusChangedAt();
    }
}
