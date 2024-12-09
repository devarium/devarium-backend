package io.devarium.core.domain.post.service;

import io.devarium.core.domain.post.Post;
import io.devarium.core.domain.post.command.UpsertPostCommand;
import io.devarium.core.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public Post createPost(UpsertPostCommand command) {
        Post post = Post.builder()
            .title(command.title())
            .content(command.content())
            .build();
        return postRepository.save(post);
    }
}
