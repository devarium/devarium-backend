package io.devarium.core.domain.comment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import io.devarium.core.domain.comment.Comment;
import io.devarium.core.domain.comment.command.UpsertCommentCommand;
import io.devarium.core.domain.comment.exception.CommentException;
import io.devarium.core.domain.comment.repository.CommentRepository;
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

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    private record TestUpsertCommentCommand(
        String content
    ) implements UpsertCommentCommand {

    }

    @Nested
    class CreateCommentTest {

        @Test
        void givenValidCommentCommand_whenCreateComment_thenCommentIsSaved() {
            // given
            UpsertCommentCommand command = new TestUpsertCommentCommand(CONTENT);

            Comment expectedComment = Comment.builder()
                .content(CONTENT)
                .build();

            Comment savedComment = Comment.builder()
                .id(COMMENT_ID)
                .content(CONTENT)
                .build();

            given(commentRepository.save(any(Comment.class))).willReturn(savedComment);

            // when
            Comment createdComment = commentService.createComment(command);

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
        void givenNonExistingComment_whenGetComment_thenCommentIsNotFound() {
            // given
            given(commentRepository.findById(NON_EXISTENT_ID))
                .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> commentService.getComment(NON_EXISTENT_ID))
                .isInstanceOf(CommentException.class)
                .hasMessageContaining("Comment not found with id:");

            then(commentRepository).should().findById(NON_EXISTENT_ID);
        }
    }

    @Nested
    class UpdateCommentTest {

        @Test
        void givenExistingCommentAndValidCommentCommand_whenUpdateComment_thenCommentIsUpdated() {
            // given
            String updatedContent = "updated content";
            UpsertCommentCommand command = new TestUpsertCommentCommand(updatedContent);

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
            Comment updatedComment = commentService.updateComment(COMMENT_ID, command);

            // then
            then(commentRepository).should().findById(COMMENT_ID);
            then(commentRepository).should().save(refEq(savedComment));

            assertThat(updatedComment)
                .extracting(Comment::getId, Comment::getContent)
                .containsExactly(COMMENT_ID, updatedContent);
        }

        @Test
        void givenNonExistingCommentAndValidCommentCommand_whenUpdateComment_thenCommentIsNotFound() {
            // given
            UpsertCommentCommand command = new TestUpsertCommentCommand(CONTENT);
            given(commentRepository.findById(NON_EXISTENT_ID)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> commentService.updateComment(NON_EXISTENT_ID, command))
                .isInstanceOf(CommentException.class)
                .hasMessageContaining("Comment not found with id:");

            then(commentRepository).should().findById(NON_EXISTENT_ID);
            then(commentRepository).shouldHaveNoMoreInteractions();
        }
    }

    @Nested
    class DeleteCommentTest {

        @Test
        void givenCommentId_whenDeleteComment_thenCommentIsDeleted() {
            // when
            commentService.deleteComment(COMMENT_ID);

            // then
            then(commentRepository).should().deleteById(COMMENT_ID);
        }
    }
}