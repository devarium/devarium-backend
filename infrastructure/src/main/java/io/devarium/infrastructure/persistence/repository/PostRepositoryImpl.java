package io.devarium.infrastructure.persistence.repository;

import io.devarium.core.domain.post.Post;
import io.devarium.core.domain.post.repository.PostRepository;
import io.devarium.infrastructure.persistence.entity.PostEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PostRepositoryImpl implements PostRepository {

    private final PostJpaRepository postJpaRepository;

    @Override
    public Post save(Post post) {
        PostEntity entity = convertToEntity(post);
        PostEntity savedEntity = postJpaRepository.save(entity);
        return convertToDomain(savedEntity);
    }

    @Override
    public Optional<Post> findById(Long id) {
        return postJpaRepository.findById(id).map(this::convertToDomain);
    }

    private Post convertToDomain(PostEntity entity) {
        return Post.builder()
            .id(entity.getId())
            .title(entity.getTitle())
            .content(entity.getContent())
            .build();
    }

    private PostEntity convertToEntity(Post post) {
        return PostEntity.builder()
            .title(post.getTitle())
            .content(post.getContent())
            .build();
    }
}
