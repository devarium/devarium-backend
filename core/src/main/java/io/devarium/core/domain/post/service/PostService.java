package io.devarium.core.domain.post.service;

import io.devarium.core.domain.post.Post;
import io.devarium.core.domain.post.port.UpsertPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {

    Post createPost(UpsertPost request);

    Post getPost(Long postId);
  
    Page<Post> getAllPosts(Pageable pageable);

    Post updatePost(Long postId, UpsertPost request);

    void deletePost(Long postId);
}
