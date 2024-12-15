package io.devarium.core.domain.post.service;

import io.devarium.core.domain.post.Post;
import io.devarium.core.domain.post.port.UpsertPost;

public interface PostService {

    Post createPost(UpsertPost request);

    Post getPost(Long postId);

    Post updatePost(Long postId, UpsertPost request);

    void deletePost(Long postId);
}
