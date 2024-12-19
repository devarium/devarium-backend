package io.devarium.core.domain.post.repository;

import io.devarium.core.domain.post.Post;
import java.util.Optional;

public interface PostRepository {

    Post save(Post post);

    void deleteWithCommentsByPostId(Long id);

    Optional<Post> findById(Long id);
}
