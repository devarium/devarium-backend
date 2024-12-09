package io.devarium.core.domain.post.service;

import io.devarium.core.domain.post.Post;
import io.devarium.core.domain.post.command.UpsertPostCommand;
import io.devarium.core.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    public Post createPost(
        UpsertPostCommand command
        // TODO: User user
    ) {
        Post post = Post.builder()
            .title(command.title())
            .content(command.content())
            .build();

        return postRepository.save(post);
    }
}
