package io.devarium.core.domain.comment;

import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Comment {

    public static final int DEFAULT_PAGE_SIZE = 5;

    private final Long id;
    private final Instant createdAt;
    private String content;
    private final Long postId;
    private final Long authorId;

    @Builder
    public Comment(Long id, String content, Instant createdAt, Long postId, Long authorId) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.postId = postId;
        this.authorId = authorId;

    }

    public void updateContent(String content) {
        this.content = content;
    }
}
