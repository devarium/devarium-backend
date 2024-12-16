package io.devarium.core.domain.reply.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import io.devarium.core.domain.reply.Reply;
import io.devarium.core.domain.reply.exception.ReplyException;
import io.devarium.core.domain.reply.port.UpsertReply;
import io.devarium.core.domain.reply.repository.ReplyRepository;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ReplyServiceImplTest {

    private static final Long REPLY_ID = 1L;
    private static final Long NON_EXISTENT_ID = 999L;
    private static final String CONTENT = "content";

    @Mock
    private ReplyRepository replyRepository;

    @InjectMocks
    private ReplyServiceImpl replyService;

    private record TestUpsertReply(String content) implements UpsertReply {

    }

    @Nested
    class CreateReplyTest {

        @Test
        void givenValidReplyRequest_whenCreateReply_thenReplyIsSaved() {
            // given
            UpsertReply request = new TestUpsertReply(CONTENT);

            Reply expectedReply = Reply.builder()
                .content(CONTENT)
                .build();

            Reply savedReply = Reply.builder()
                .id(REPLY_ID)
                .content(CONTENT)
                .build();

            given(replyRepository.save(any(Reply.class))).willReturn(savedReply);

            // when
            Reply createdReply = replyService.createReply(request);

            // then
            then(replyRepository).should().save(refEq(expectedReply));

            assertThat(createdReply)
                .extracting(Reply::getId, Reply::getContent)
                .containsExactly(REPLY_ID, CONTENT);
        }
    }

    @Nested
    class GetReplyTest {

        @Test
        void givenExistingReply_whenGetReply_thenReplyIsFound() {
            // given
            Reply expectedReply = Reply.builder()
                .id(REPLY_ID)
                .content(CONTENT)
                .build();

            given(replyRepository.findById(REPLY_ID)).willReturn(Optional.of(expectedReply));

            // when
            Reply foundReply = replyService.getReply(REPLY_ID);

            // then
            then(replyRepository).should().findById(REPLY_ID);

            assertThat(foundReply)
                .extracting(Reply::getId, Reply::getContent)
                .containsExactly(REPLY_ID, CONTENT);
        }

        @Test
        void givenNonExistingReply_whenGetReply_thenReplyIsNotFound() {
            // given
            given(replyRepository.findById(NON_EXISTENT_ID))
                .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> replyService.getReply(NON_EXISTENT_ID))
                .isInstanceOf(ReplyException.class);

            then(replyRepository).should().findById(NON_EXISTENT_ID);
        }
    }

    @Nested
    class UpdateReplyTest {

        @Test
        void givenExistingReplyAndValidReplyRequest_whenUpdateReply_thenReplyIsUpdated() {
            // given
            String updatedContent = "updated content";
            UpsertReply request = new TestUpsertReply(updatedContent);

            Reply existingReply = Reply.builder()
                .id(REPLY_ID)
                .content(CONTENT)
                .build();

            Reply savedReply = Reply.builder()
                .id(REPLY_ID)
                .content(updatedContent)
                .build();

            given(replyRepository.findById(REPLY_ID)).willReturn(Optional.of(existingReply));
            given(replyRepository.save(any(Reply.class))).willReturn(savedReply);

            // when
            Reply updatedReply = replyService.updateReply(REPLY_ID, request);

            // then
            then(replyRepository).should().findById(REPLY_ID);
            then(replyRepository).should().save(refEq(savedReply));

            assertThat(updatedReply)
                .extracting(Reply::getId, Reply::getContent)
                .containsExactly(REPLY_ID, updatedContent);
        }

        @Test
        void givenNonExistingReplyAndValidReplyRequest_whenUpdateReply_thenReplyIsNotFound() {
            // given
            UpsertReply request = new TestUpsertReply(CONTENT);
            given(replyRepository.findById(NON_EXISTENT_ID)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> replyService.updateReply(NON_EXISTENT_ID, request))
                .isInstanceOf(ReplyException.class);

            then(replyRepository).should().findById(NON_EXISTENT_ID);
            then(replyRepository).shouldHaveNoMoreInteractions();
        }
    }

    @Nested
    class DeleteReplyTest {

        @Test
        void givenReplyId_whenDeleteReply_thenReplyIsDeleted() {
            // when
            replyService.deleteReply(REPLY_ID);

            // then
            then(replyRepository).should().deleteById(REPLY_ID);
        }
    }
}