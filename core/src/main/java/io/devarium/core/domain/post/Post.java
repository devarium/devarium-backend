package io.devarium.core.domain.post;

import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Post {

    private final Long id;
    private final Instant createdAt;
    private String title;
    private String content;

    @Builder
    public Post(Long id, String title, String content, Instant createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
