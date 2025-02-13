package io.devarium.core.domain.post.port.in;

import io.devarium.core.domain.post.Post;
import io.devarium.core.domain.post.command.UpsertPost;
import io.devarium.core.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {

    Post createPost(UpsertPost request, User user);

    Post getPost(Long postId);

    Page<Post> getAllPosts(Pageable pageable);

    Post updatePost(Long postId, UpsertPost request, User user);

    void deletePost(Long postId, User user);
}
