package io.devarium.infrastructure.persistence.service;

import io.devarium.core.domain.post.Post;
import io.devarium.core.domain.post.command.UpsertPost;
import io.devarium.core.domain.post.port.in.PostService;
import io.devarium.core.domain.post.service.PostServiceImpl;
import io.devarium.core.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class PostServiceDecorator implements PostService {

    private final PostServiceImpl postService;

    @Override
    @Transactional
    public Post createPost(UpsertPost request, User user) {
        return postService.createPost(request, user);
    }

    @Override
    @Transactional(readOnly = true)
    public Post getPost(Long postId) {
        return postService.getPost(postId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> getAllPosts(Pageable pageable) {
        return postService.getAllPosts(pageable);
    }

    @Override
    @Transactional
    public Post updatePost(Long postId, UpsertPost request, User user) {
        return postService.updatePost(postId, request, user);
    }

    @Override
    @Transactional
    public void deletePost(Long postId, User user) {
        postService.deletePost(postId, user);
    }
}
