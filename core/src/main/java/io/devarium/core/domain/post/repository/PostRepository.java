package io.devarium.core.domain.post.repository;

import io.devarium.core.domain.post.Post;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepository {

    Post save(Post post);

    void deleteById(Long id);

    Optional<Post> findById(Long id);

    Page<Post> findAll(Pageable pageable);

    boolean existsById(Long id);
}
