package io.devarium.infrastructure.persistence.entity;

import io.devarium.core.domain.post.Post;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    private String content;

    @Builder
    public PostEntity(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public void update(Post post) {
        this.title = post.getTitle();
        this.content = post.getContent();
    }
}
