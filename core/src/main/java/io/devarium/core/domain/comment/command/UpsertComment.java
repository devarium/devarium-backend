package io.devarium.core.domain.comment.command;

public interface UpsertComment {

    String content();

    Long postId();
}
