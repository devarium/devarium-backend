package io.devarium.infrastructure.persistence.service;

import io.devarium.core.domain.post.Post;
import io.devarium.core.domain.post.port.UpsertPost;
import io.devarium.core.domain.post.service.PostService;
import io.devarium.core.domain.post.service.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class PostServiceDecorator implements PostService {

    private final PostServiceImpl postService;

    @Override
    @Transactional
    public Post createPost(UpsertPost request) {
        return postService.createPost(request);
    }

    @Override
    @Transactional(readOnly = true)
    public Post getPost(Long postId) {
        return postService.getPost(postId);
    }

    @Override
    @Transactional
    public Post updatePost(Long postId, UpsertPost request) {
        return postService.updatePost(postId, request);
    }

    @Override
    @Transactional
    public void deletePost(Long postId) {
        postService.deletePost(postId);
    }
}
