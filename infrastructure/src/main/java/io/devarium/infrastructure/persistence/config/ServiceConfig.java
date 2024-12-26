package io.devarium.infrastructure.persistence.config;

import io.devarium.core.domain.comment.repository.CommentRepository;
import io.devarium.core.domain.comment.service.CommentService;
import io.devarium.core.domain.comment.service.CommentServiceImpl;
import io.devarium.core.domain.post.repository.PostRepository;
import io.devarium.core.domain.post.service.PostService;
import io.devarium.core.domain.post.service.PostServiceImpl;
import io.devarium.core.domain.reply.repository.ReplyRepository;
import io.devarium.core.domain.reply.service.ReplyService;
import io.devarium.core.domain.reply.service.ReplyServiceImpl;
import io.devarium.core.domain.user.repository.UserRepository;
import io.devarium.core.domain.user.service.UserService;
import io.devarium.core.domain.user.service.UserServiceImpl;
import io.devarium.infrastructure.persistence.service.CommentServiceDecorator;
import io.devarium.infrastructure.persistence.service.PostServiceDecorator;
import io.devarium.infrastructure.persistence.service.ReplyServiceDecorator;
import io.devarium.infrastructure.persistence.service.UserServiceDecorator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public PostService postService(PostRepository postRepository) {
        return new PostServiceDecorator(new PostServiceImpl(postRepository));
    }

    @Bean
    public CommentService commentService(CommentRepository commentRepository,
        ReplyService replyService) {
        return new CommentServiceDecorator(new CommentServiceImpl(commentRepository, replyService));
    }

    @Bean
    public ReplyService replyService(ReplyRepository replyRepository) {
        return new ReplyServiceDecorator(new ReplyServiceImpl(replyRepository));
    }

    @Bean
    public UserService userService(UserRepository userRepository) {
        return new UserServiceDecorator(new UserServiceImpl(userRepository));
    }
}
