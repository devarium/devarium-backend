package io.devarium.infrastructure.persistence.config;

import io.devarium.core.domain.comment.port.in.CommentService;
import io.devarium.core.domain.comment.port.out.CommentRepository;
import io.devarium.core.domain.comment.service.CommentServiceImpl;
import io.devarium.core.domain.feedback.port.in.FeedbackService;
import io.devarium.core.domain.feedback.port.out.AnswerRepository;
import io.devarium.core.domain.feedback.port.out.QuestionRepository;
import io.devarium.core.domain.feedback.port.out.TextSummarizer;
import io.devarium.core.domain.feedback.service.FeedbackServiceImpl;
import io.devarium.core.domain.like.port.in.LikeService;
import io.devarium.core.domain.like.port.out.LikeRepository;
import io.devarium.core.domain.like.service.LikeServiceImpl;
import io.devarium.core.domain.member.port.in.MemberService;
import io.devarium.core.domain.member.port.out.MemberRepository;
import io.devarium.core.domain.member.service.MemberServiceImpl;
import io.devarium.core.domain.post.port.in.PostService;
import io.devarium.core.domain.post.port.out.PostRepository;
import io.devarium.core.domain.post.service.PostServiceImpl;
import io.devarium.core.domain.project.port.in.ProjectService;
import io.devarium.core.domain.project.port.out.ProjectRepository;
import io.devarium.core.domain.project.service.ProjectServiceImpl;
import io.devarium.core.domain.reply.port.in.ReplyService;
import io.devarium.core.domain.reply.port.out.ReplyRepository;
import io.devarium.core.domain.reply.service.ReplyServiceImpl;
import io.devarium.core.domain.team.port.in.TeamService;
import io.devarium.core.domain.team.port.out.TeamRepository;
import io.devarium.core.domain.team.service.TeamServiceImpl;
import io.devarium.core.domain.user.port.in.UserService;
import io.devarium.core.domain.user.port.out.UserRepository;
import io.devarium.core.domain.user.service.UserServiceImpl;
import io.devarium.core.storage.port.in.StorageService;
import io.devarium.infrastructure.persistence.service.CommentServiceDecorator;
import io.devarium.infrastructure.persistence.service.FeedbackServiceDecorator;
import io.devarium.infrastructure.persistence.service.LikeServiceDecorator;
import io.devarium.infrastructure.persistence.service.MemberServiceDecorator;
import io.devarium.infrastructure.persistence.service.PostServiceDecorator;
import io.devarium.infrastructure.persistence.service.ProjectServiceDecorator;
import io.devarium.infrastructure.persistence.service.ReplyServiceDecorator;
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
    public UserService userService(UserRepository userRepository, StorageService storageService) {
        return new UserServiceDecorator(new UserServiceImpl(userRepository, storageService));
    }

    @Bean
    public ProjectService projectService(ProjectRepository projectRepository) {
        return new ProjectServiceDecorator(new ProjectServiceImpl(projectRepository));
    }

    @Bean
    public TeamService teamService(TeamRepository teamRepository, MemberService memberService) {
        return new TeamServiceDecorator(new TeamServiceImpl(teamRepository, memberService));
    }

    @Bean
    public MemberService memberService(MemberRepository memberRepository) {
        return new MemberServiceDecorator(new MemberServiceImpl(memberRepository));
    }

    @Bean
    public FeedbackService feedbackService(
        QuestionRepository questionRepository,
        AnswerRepository answerRepository,
        ProjectService projectService,
        MemberService memberService,
        TextSummarizer textSummarizer
    ) {
        return new FeedbackServiceDecorator(
            new FeedbackServiceImpl(
                questionRepository,
                answerRepository,
                projectService,
                memberService,
                textSummarizer
            )
        );
    }

    @Bean
    public LikeService likeService(LikeRepository likeRepository) {
        return new LikeServiceDecorator(new LikeServiceImpl(likeRepository));
    }
}
