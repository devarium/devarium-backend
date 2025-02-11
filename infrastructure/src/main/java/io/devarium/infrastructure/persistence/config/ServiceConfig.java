package io.devarium.infrastructure.persistence.config;

import io.devarium.core.domain.comment.repository.CommentRepository;
import io.devarium.core.domain.comment.service.CommentService;
import io.devarium.core.domain.comment.service.CommentServiceImpl;
import io.devarium.core.domain.feedback.answer.repository.AnswerRepository;
import io.devarium.core.domain.feedback.port.TextSummarizer;
import io.devarium.core.domain.feedback.question.repository.QuestionRepository;
import io.devarium.core.domain.feedback.service.FeedbackService;
import io.devarium.core.domain.feedback.service.FeedbackServiceImpl;
import io.devarium.core.domain.like.repository.LikeRepository;
import io.devarium.core.domain.like.service.LikeService;
import io.devarium.core.domain.like.service.LikeServiceImpl;
import io.devarium.core.domain.membership.repository.MembershipRepository;
import io.devarium.core.domain.membership.service.MembershipService;
import io.devarium.core.domain.membership.service.MembershipServiceImpl;
import io.devarium.core.domain.post.repository.PostRepository;
import io.devarium.core.domain.post.service.PostService;
import io.devarium.core.domain.post.service.PostServiceImpl;
import io.devarium.core.domain.project.repository.ProjectRepository;
import io.devarium.core.domain.project.service.ProjectService;
import io.devarium.core.domain.project.service.ProjectServiceImpl;
import io.devarium.core.domain.reply.repository.ReplyRepository;
import io.devarium.core.domain.reply.service.ReplyService;
import io.devarium.core.domain.reply.service.ReplyServiceImpl;
import io.devarium.core.domain.team.repository.TeamRepository;
import io.devarium.core.domain.team.service.TeamService;
import io.devarium.core.domain.team.service.TeamServiceImpl;
import io.devarium.core.domain.teamRequest.repository.TeamRequestRepository;
import io.devarium.core.domain.teamRequest.service.TeamRequestService;
import io.devarium.core.domain.teamRequest.service.TeamRequestServiceImpl;
import io.devarium.core.domain.user.repository.UserRepository;
import io.devarium.core.domain.user.service.UserService;
import io.devarium.core.domain.user.service.UserServiceImpl;
import io.devarium.core.storage.service.StorageService;
import io.devarium.infrastructure.persistence.service.CommentServiceDecorator;
import io.devarium.infrastructure.persistence.service.FeedbackServiceDecorator;
import io.devarium.infrastructure.persistence.service.LikeServiceDecorator;
import io.devarium.infrastructure.persistence.service.MembershipServiceDecorator;
import io.devarium.infrastructure.persistence.service.PostServiceDecorator;
import io.devarium.infrastructure.persistence.service.ProjectServiceDecorator;
import io.devarium.infrastructure.persistence.service.ReplyServiceDecorator;
import io.devarium.infrastructure.persistence.service.TeamRequestServiceDecorator;
import io.devarium.infrastructure.persistence.service.TeamServiceDecorator;
import io.devarium.infrastructure.persistence.service.UserServiceDecorator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public PostService postService(PostRepository postRepository, CommentService commentService) {
        return new PostServiceDecorator(new PostServiceImpl(postRepository, commentService));
    }

    @Bean
    public CommentService commentService(
        CommentRepository commentRepository,
        ReplyService replyService
    ) {
        return new CommentServiceDecorator(new CommentServiceImpl(commentRepository, replyService));
    }

    @Bean
    public ReplyService replyService(ReplyRepository replyRepository) {
        return new ReplyServiceDecorator(new ReplyServiceImpl(replyRepository));
    }

    @Bean
    public UserService userService(
        UserRepository userRepository,
        StorageService storageService,
        TeamService teamService,
        MembershipService membershipService,
        TeamRequestService teamRequestService
    ) {
        return new UserServiceDecorator(
            new UserServiceImpl(
                userRepository,
                storageService,
                teamService,
                membershipService,
                teamRequestService
            ));
    }

    @Bean
    public ProjectService projectService(ProjectRepository projectRepository) {
        return new ProjectServiceDecorator(new ProjectServiceImpl(projectRepository));
    }

    @Bean
    public TeamService teamService(
        TeamRepository teamRepository,
        MembershipService membershipService,
        StorageService storageService
    ) {
        return new TeamServiceDecorator(
            new TeamServiceImpl(teamRepository, membershipService, storageService));
    }

    @Bean
    public MembershipService membershipService(
        MembershipRepository membershipRepository
    ) {
        return new MembershipServiceDecorator(
            new MembershipServiceImpl(membershipRepository));
    }

    @Bean
    public TeamRequestService teamRequestService(
        TeamRequestRepository teamRequestRepository,
        MembershipService membershipService,
        TeamService teamService,
        UserService userService
    ) {
        return new TeamRequestServiceDecorator(
            new TeamRequestServiceImpl(
                teamRequestRepository,
                membershipService,
                teamService,
                userService
            ));
    }

    @Bean
    public FeedbackService feedbackService(
        QuestionRepository questionRepository,
        AnswerRepository answerRepository,
        ProjectService projectService,
        MembershipService membershipService,
        TextSummarizer textSummarizer
    ) {
        return new FeedbackServiceDecorator(
            new FeedbackServiceImpl(
                questionRepository,
                answerRepository,
                projectService,
                membershipService,
                textSummarizer
            )
        );
    }

    @Bean
    public LikeService likeService(LikeRepository likeRepository) {
        return new LikeServiceDecorator(new LikeServiceImpl(likeRepository));
    }
}
