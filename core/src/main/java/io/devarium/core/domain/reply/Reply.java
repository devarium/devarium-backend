package io.devarium.core.domain.reply;

import io.devarium.core.domain.comment.Comment;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Reply {

    private final Long id;
    private final Instant createdAt;
    private String content;
    private final Comment comment;

    @Builder
    public Reply(Long id, String content, Instant createdAt, Comment comment) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.comment =comment;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
