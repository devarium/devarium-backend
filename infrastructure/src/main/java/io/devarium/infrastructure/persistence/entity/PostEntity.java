package io.devarium.infrastructure.persistence.entity;

import io.devarium.core.domain.post.Post;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Builder
    private PostEntity(Long id, String title, String content, UserEntity user) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public static PostEntity fromDomain(Post post, UserEntity userEntity) {
        return PostEntity.builder()
            .title(post.getTitle())
            .content(post.getContent())
            .user(userEntity)
            .build();
    }

    public Post toDomain() {
        return Post.builder()
            .id(id)
            .title(title)
            .content(content)
            .userId(user.getId())
            .createdAt(getCreatedAt())
            .build();
    }

    public void update(Post post) {
        this.title = post.getTitle();
        this.content = post.getContent();
    }
}
