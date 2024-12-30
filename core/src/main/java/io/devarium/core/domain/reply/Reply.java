package io.devarium.core.domain.reply;

import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Reply {

    public static final int DEFAULT_PAGE_SIZE = 3;

    private final Long id;
    private final Instant createdAt;
    private String content;
    private final Long commentId;
    private final Long authorId;


    @Builder
    public Reply(Long id, String content, Instant createdAt, Long commentId, Long authorId) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.commentId = commentId;
        this.authorId = authorId;

    }

    public void updateContent(String content) {
        this.content = content;
    }
}
