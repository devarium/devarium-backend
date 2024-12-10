package io.devarium.infrastructure.persistence.config;

import io.devarium.core.domain.comment.repository.CommentRepository;
import io.devarium.core.domain.comment.service.CommentService;
import io.devarium.core.domain.comment.service.CommentServiceImpl;
import io.devarium.core.domain.post.repository.PostRepository;
import io.devarium.core.domain.post.service.PostService;
import io.devarium.core.domain.post.service.PostServiceImpl;
import io.devarium.infrastructure.persistence.service.CommentServiceDecorator;
import io.devarium.infrastructure.persistence.service.PostServiceDecorator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public PostService postService(PostRepository postRepository) {
        return new PostServiceDecorator(new PostServiceImpl(postRepository));
    }

    @Bean
    public CommentService commentService(CommentRepository commentRepository) {
        return new CommentServiceDecorator(new CommentServiceImpl(commentRepository));
    }
}
