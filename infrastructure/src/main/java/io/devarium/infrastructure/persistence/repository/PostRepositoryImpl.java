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
    public void deleteById(Long id) {
        postJpaRepository.deleteById(id);
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
            .createdAt(entity.getCreatedAt())
            .build();
    }

    private PostEntity convertToEntity(Post domain) {
        return PostEntity.builder()
            .id(domain.getId())
            .title(domain.getTitle())
            .content(domain.getContent())
            .build();
    }
}
