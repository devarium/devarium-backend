package io.devarium.core.domain.comment;

import io.devarium.core.domain.reply.Reply;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Comment  {

    private final Long id;
    private final Instant createdAt;
    private String content;
    private final List<Reply> replies = new ArrayList<>();

    @Builder
    public Comment(Long id, String content, Instant createdAt) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void addReply(Reply reply) {
        replies.add(reply);
    }

    public void removeReply(Reply reply) {
        replies.remove(reply);
    }
}