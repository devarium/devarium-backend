package io.devarium.infrastructure.persistence.entity;

import io.devarium.core.domain.comment.Comment;
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
@Table(name = "comments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Builder
    private CommentEntity(Long id, String content, PostEntity post, UserEntity user) {
        this.id = id;
        this.content = content;
        this.post = post;
        this.user = user;
    }

    public static CommentEntity fromDomain(
        Comment comment,
        PostEntity postEntity,
        UserEntity userEntity
    ) {
        return CommentEntity.builder()
            .content(comment.getContent())
            .post(postEntity)
            .user(userEntity)
            .build();
    }

    public Comment toDomain() {
        return Comment.builder()
            .id(id)
            .content(content)
            .postId(post.getId())
            .createdAt(getCreatedAt())
            .build();
    }

    public void update(Comment domain) {
        this.content = domain.getContent();
    }
}
