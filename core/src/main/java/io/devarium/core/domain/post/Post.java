package io.devarium.core.domain.post;

import io.devarium.core.auth.exception.AuthErrorCode;
import io.devarium.core.auth.exception.CustomAuthException;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Post {

    public static final int DEFAULT_PAGE_SIZE = 10;

    private final Long id;
    private final Long userId;
    private final Instant createdAt;
    private String title;
    private String content;

    @Builder
    public Post(Long id, String title, String content, Long userId, Instant createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public void validateAuthor(Long userId) {
        if (!this.userId.equals(userId)) {
            throw new CustomAuthException(AuthErrorCode.FORBIDDEN_ACCESS, userId, this.id);
        }
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
