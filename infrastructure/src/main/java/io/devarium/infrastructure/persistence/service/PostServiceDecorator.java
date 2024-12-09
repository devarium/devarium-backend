package io.devarium.infrastructure.persistence.service;

import io.devarium.core.domain.post.Post;
import io.devarium.core.domain.post.command.UpsertPostCommand;
import io.devarium.core.domain.post.service.PostService;
import io.devarium.core.domain.post.service.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class PostServiceDecorator implements PostService {

    private final PostServiceImpl postService;

    @Override
    @Transactional
    public Post createPost(UpsertPostCommand command) {
        return postService.createPost(command);
    }
}