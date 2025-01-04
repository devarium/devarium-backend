package io.devarium.core.domain.reply;

import io.devarium.core.auth.exception.AuthErrorCode;
import io.devarium.core.auth.exception.CustomAuthException;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Reply {

    public static final int DEFAULT_PAGE_SIZE = 3;

    private final Long id;
    private final Long commentId;
    private final Long userId;
    private final Instant createdAt;

    private String content;

    @Builder
    public Reply(Long id, String content, Long commentId, Long userId, Instant createdAt) {
        this.id = id;
        this.content = content;
        this.commentId = commentId;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public void validateAuthor(Long userId) {
        if (!this.userId.equals(userId)) {
            throw new CustomAuthException(AuthErrorCode.FORBIDDEN_ACCESS, id);
        }
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
