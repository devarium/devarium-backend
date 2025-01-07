package io.devarium.infrastructure.persistence.entity;

import io.devarium.core.domain.member.Member;
import io.devarium.core.domain.member.MemberRole;
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
    name = "members",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "team_id"})
)
public class MemberEntity extends BaseEntity {

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

    @Builder
    private MemberEntity(UserEntity user, TeamEntity team, MemberRole role) {
        this.user = user;
        this.team = team;
        this.role = role;
    }

    public static MemberEntity fromDomain(Member member, UserEntity user, TeamEntity team) {
        return MemberEntity.builder()
            .user(user)
            .team(team)
            .role(member.getRole())
            .build();
    }

    public Member toDomain() {
        return Member.builder()
            .id(id)
            .userId(user.getId())
            .teamId(team.getId())
            .role(role)
            .build();
    }

    public void update(MemberRole role) {
        this.role = role;
    }
}
