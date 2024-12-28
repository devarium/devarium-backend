package io.devarium.infrastructure.persistence.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.devarium.core.domain.post.Post;
import io.devarium.core.domain.post.exception.PostErrorCode;
import io.devarium.core.domain.post.exception.PostException;
import io.devarium.core.domain.post.repository.PostRepository;
import io.devarium.core.domain.user.exception.UserErrorCode;
import io.devarium.core.domain.user.exception.UserException;
import io.devarium.infrastructure.persistence.entity.PostEntity;
import io.devarium.infrastructure.persistence.entity.QPostEntity;
import io.devarium.infrastructure.persistence.entity.UserEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PostRepositoryImpl implements PostRepository {

    private final PostJpaRepository postJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Post save(Post post) {
        PostEntity entity = convertToEntity(post);
        PostEntity savedEntity = postJpaRepository.save(entity);
        return convertToDomain(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        QPostEntity post = QPostEntity.postEntity;
        queryFactory.delete(post)
            .where(post.id.eq(id))
            .execute();
    }

    @Override
    public Optional<Post> findById(Long id) {
        return postJpaRepository.findById(id).map(this::convertToDomain);
    }

    @Override
    public Page<Post> findAll(Pageable pageable) {
        return postJpaRepository.findAll(pageable).map(this::convertToDomain);
    }

    private Post convertToDomain(PostEntity entity) {
        return Post.builder()
            .id(entity.getId())
            .title(entity.getTitle())
            .content(entity.getContent())
            .createdAt(entity.getCreatedAt())
            .authorId(entity.getAuthor().getId())
            .build();
    }

    private PostEntity convertToEntity(Post domain) {
        if (domain.getId() != null) {
            PostEntity entity = postJpaRepository.findById(domain.getId())
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND, domain.getId()));
            entity.update(domain);
            return entity;
        }

        UserEntity author = userJpaRepository.findById(domain.getAuthorId()).orElseThrow(
            () -> new UserException(UserErrorCode.USER_NOT_FOUND, domain.getAuthorId())
        );
        return PostEntity.builder()
            .title(domain.getTitle())
            .content(domain.getContent())
            .author(author)
            .build();
    }
}
