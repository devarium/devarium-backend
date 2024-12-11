package io.devarium.core.domain.post.service;

import io.devarium.core.domain.post.Post;
import io.devarium.core.domain.post.command.UpsertPostCommand;

public interface PostService {

    Post createPost(UpsertPostCommand command);
}
