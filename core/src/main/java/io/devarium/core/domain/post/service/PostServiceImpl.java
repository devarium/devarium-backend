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

    @Override
    public Post getPost(Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new RuntimeException("Post not found."));
    }

    @Override
    public Post updatePost(Long postId, UpsertPostCommand command) {
        Post post = getPost(postId);
        post.updateTitle(command.title());
        post.updateContent(command.content());
        return postRepository.save(post);
    }

    @Override
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }
}
