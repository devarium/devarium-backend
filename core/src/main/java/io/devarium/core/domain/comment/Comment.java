package io.devarium.core.domain.comment;

import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Comment {

    public static final int DEFAULT_PAGE_SIZE = 5;

    private final Long id;
    private final Long postId;
    private final Long userId;
    private final Instant createdAt;

    private String content;

    @Builder
    public Comment(Long id, String content, Long postId, Long userId, Instant createdAt) {
        this.id = id;
        this.content = content;
        this.postId = postId;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
