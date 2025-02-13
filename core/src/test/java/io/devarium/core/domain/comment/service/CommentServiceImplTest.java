package io.devarium.core.domain.comment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import io.devarium.core.auth.OAuth2Provider;
import io.devarium.core.domain.comment.Comment;
import io.devarium.core.domain.comment.command.UpsertComment;
import io.devarium.core.domain.comment.exception.CommentException;
import io.devarium.core.domain.comment.port.out.CommentRepository;
import io.devarium.core.domain.user.User;
import io.devarium.core.domain.user.UserRole;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {

    private static final Long COMMENT_ID = 1L;
    private static final Long NON_EXISTENT_ID = 999L;
    private static final String CONTENT = "content";
    private static final Long POST_ID = 2L;
    private static final User USER = User.builder()
        .id(10L)
        .email("testUser@email.com")
        .username("testUser")
        .bio("bio")
        .profileImageUrl("profileImageUrl")
        .blogUrl("blogUrl")
        .githubUrl("githubUrl")
        .role(UserRole.USER)
        .provider(OAuth2Provider.GOOGLE)
        .build();

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    private record TestUpsertComment(String content, Long postId) implements UpsertComment {

    }

    @Nested
    class CreateCommentTest {

        @Test
        void givenValidCommentRequest_whenCreateComment_thenCommentIsSaved() {
            // given
            UpsertComment request = new TestUpsertComment(CONTENT, POST_ID);

            Comment expectedComment = Comment.builder()
                .content(CONTENT)
                .build();

            Comment savedComment = Comment.builder()
                .id(COMMENT_ID)
                .content(CONTENT)
                .build();

            given(commentRepository.save(any(Comment.class))).willReturn(savedComment);

            // when
            Comment createdComment = commentService.createComment(request, USER);

            // then
            then(commentRepository).should().save(refEq(expectedComment));

            assertThat(createdComment)
                .extracting(Comment::getId, Comment::getContent)
                .containsExactly(COMMENT_ID, CONTENT);
        }
    }

    @Nested
    class GetCommentTest {

        @Test
        void givenExistingComment_whenGetComment_thenCommentIsFound() {
            // given
            Comment expectedComment = Comment.builder()
                .id(COMMENT_ID)
                .content(CONTENT)
                .build();

            given(commentRepository.findById(COMMENT_ID)).willReturn(Optional.of(expectedComment));

            // when
            Comment foundComment = commentService.getComment(COMMENT_ID);

            // then
            then(commentRepository).should().findById(COMMENT_ID);

            assertThat(foundComment)
                .extracting(Comment::getId, Comment::getContent)
                .containsExactly(COMMENT_ID, CONTENT);
        }

        @Test
        void givenNonExistentComment_whenGetComment_thenCommentIsNotFound() {
            // given
            given(commentRepository.findById(NON_EXISTENT_ID))
                .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> commentService.getComment(NON_EXISTENT_ID))
                .isInstanceOf(CommentException.class);

            then(commentRepository).should().findById(NON_EXISTENT_ID);
        }
    }

    @Nested
    class UpdateCommentTest {

        @Test
        void givenExistingCommentAndValidCommentRequest_whenUpdateComment_thenCommentIsUpdated() {
            // given
            String updatedContent = "updated content";
            UpsertComment request = new TestUpsertComment(updatedContent, POST_ID);

            Comment existingComment = Comment.builder()
                .id(COMMENT_ID)
                .content(CONTENT)
                .build();

            Comment savedComment = Comment.builder()
                .id(COMMENT_ID)
                .content(updatedContent)
                .build();

            given(commentRepository.findById(COMMENT_ID)).willReturn(Optional.of(existingComment));
            given(commentRepository.save(any(Comment.class))).willReturn(savedComment);

            // when
            Comment updatedComment = commentService.updateComment(COMMENT_ID, request, USER);

            // then
            then(commentRepository).should().findById(COMMENT_ID);
            then(commentRepository).should().save(refEq(savedComment));

            assertThat(updatedComment)
                .extracting(Comment::getId, Comment::getContent)
                .containsExactly(COMMENT_ID, updatedContent);
        }

        @Test
        void givenNonExistentCommentAndValidCommentRequest_whenUpdateComment_thenCommentIsNotFound() {
            // given
            UpsertComment request = new TestUpsertComment(CONTENT, POST_ID);
            given(commentRepository.findById(NON_EXISTENT_ID)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> commentService.updateComment(NON_EXISTENT_ID, request, USER))
                .isInstanceOf(CommentException.class);

            then(commentRepository).should().findById(NON_EXISTENT_ID);
            then(commentRepository).shouldHaveNoMoreInteractions();
        }
    }

    @Nested
    class DeleteCommentTest {

        @Test
        void givenCommentId_whenDeleteComment_thenCommentIsDeleted() {
            // when
            commentService.deleteComment(COMMENT_ID, USER);

            // then
            then(commentRepository).should().deleteById(COMMENT_ID);
        }
    }
}
