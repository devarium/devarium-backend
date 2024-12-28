package io.devarium.core.domain.post;

import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Post {

    public static final int DEFAULT_PAGE_SIZE = 10;

    private final Long id;
    private final Instant createdAt;
    private String title;
    private String content;
    private final Long authorId;

    @Builder
    public Post(Long id, String title, String content, Instant createdAt, Long authorId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.authorId = authorId;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
