package io.devarium.core.domain.post.service;

import io.devarium.core.domain.post.Post;
import io.devarium.core.domain.post.command.UpsertPostCommand;

public interface PostService {

    Post createPost(UpsertPostCommand command);

    Post getPost(Long postId);

    Post updatePost(Long postId, UpsertPostCommand command);

    void deletePost(Long postId);
}
