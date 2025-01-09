package io.devarium.infrastructure.persistence.repository;

import io.devarium.core.domain.post.Post;
import io.devarium.core.domain.post.exception.PostErrorCode;
import io.devarium.core.domain.post.exception.PostException;
import io.devarium.core.domain.post.repository.PostRepository;
import io.devarium.infrastructure.persistence.entity.PostEntity;
import io.devarium.infrastructure.persistence.entity.UserEntity;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PostRepositoryImpl implements PostRepository {

    private final EntityManager entityManager;
    private final PostJpaRepository postJpaRepository;

    @Override
    public Post save(Post post) {
        if (post.getId() != null) {
            PostEntity entity = postJpaRepository.findById(post.getId())
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND, post.getId()));
            entity.update(post);
            return postJpaRepository.save(entity).toDomain();
        }

        UserEntity user = entityManager.getReference(UserEntity.class, post.getUserId());

        PostEntity entity = PostEntity.fromDomain(post, user);
        return postJpaRepository.save(entity).toDomain();
    }

    @Override
    public void deleteById(Long id) {
        postJpaRepository.deleteById(id);
    }

    @Override
    public Optional<Post> findById(Long id) {
        return postJpaRepository.findById(id).map(PostEntity::toDomain);
    }

    @Override
    public Page<Post> findAll(Pageable pageable) {
        return postJpaRepository.findAll(pageable).map(PostEntity::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return postJpaRepository.existsById(id);
    }
}
