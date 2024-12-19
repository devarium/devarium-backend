package io.devarium.core.domain.comment;

import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Comment {

    private final Long id;
    private final Instant createdAt;
    private String content;
    private final Long postId;

    @Builder
    public Comment(Long id, String content, Instant createdAt, Long postId) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.postId = postId;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
