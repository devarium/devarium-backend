package io.devarium.core.domain.reply;

import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Reply {

    private final Long id;
    private final Instant createdAt;
    private String content;

    @Builder
    public Reply(Long id, String content, Instant createdAt) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
