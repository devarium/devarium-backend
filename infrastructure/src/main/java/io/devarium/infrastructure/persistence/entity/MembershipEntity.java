package io.devarium.infrastructure.persistence.entity;

import io.devarium.core.domain.membership.MemberRole;
import io.devarium.core.domain.membership.Membership;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    name = "memberships",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "team_id"})
)
public class MembershipEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private TeamEntity team;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Column(nullable = false)
    @Getter(AccessLevel.NONE)
    private Boolean isLeader;

    @Builder
    private MembershipEntity(UserEntity user, TeamEntity team, MemberRole role, Boolean isLeader) {
        this.user = user;
        this.team = team;
        this.role = role;
        this.isLeader = isLeader;
    }

    public static MembershipEntity fromDomain(Membership membership, UserEntity user,
        TeamEntity team) {
        return MembershipEntity.builder()
            .user(user)
            .team(team)
            .role(membership.getRole())
            .isLeader(membership.isLeader())
            .build();
    }

    public Membership toDomain() {
        return Membership.builder()
            .id(id)
            .userId(user.getId())
            .teamId(team.getId())
            .role(role)
            .isLeader(isLeader)
            .build();
    }

    public void update(MemberRole role, Boolean isLeader) {
        this.role = role;
        this.isLeader = isLeader;
    }

    public Boolean isLeader() {
        return this.isLeader;
    }
}
