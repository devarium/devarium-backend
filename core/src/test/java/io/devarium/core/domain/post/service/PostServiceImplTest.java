package io.devarium.core.domain.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import io.devarium.core.auth.OAuth2Provider;
import io.devarium.core.domain.post.Post;
import io.devarium.core.domain.post.exception.PostException;
import io.devarium.core.domain.post.port.UpsertPost;
import io.devarium.core.domain.post.repository.PostRepository;
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
public class PostServiceImplTest {

    private static final Long POST_ID = 1L;
    private static final Long NON_EXISTENT_ID = 999L;
    private static final String TITLE = "title";
    private static final String CONTENT = "content";
    private static final User USER = new User(10L, "testUser@email.com", "testUser", "bio",
        "picture", "blogUrl", "githubUrl",
        UserRole.USER, OAuth2Provider.GOOGLE);

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostServiceImpl postService;

    private record TestUpsertPost(String title, String content) implements UpsertPost {

    }

    @Nested
    class CreatePostTest {

        @Test
        void givenValidPostRequest_whenCreatePost_thenPostIsSaved() {
            // given
            UpsertPost request = new TestUpsertPost(TITLE, CONTENT);

            Post expectedPost = Post.builder()
                .title(TITLE)
                .content(CONTENT)
                .authorId(USER.getId())
                .build();

            Post savedPost = Post.builder()
                .id(POST_ID)
                .title(TITLE)
                .content(CONTENT)
                .authorId(USER.getId())
                .build();

            given(postRepository.save(any(Post.class))).willReturn(savedPost);

            // when
            Post createdPost = postService.createPost(request, USER);

            // then
            then(postRepository).should().save(refEq(expectedPost));

            assertThat(createdPost)
                .extracting(Post::getId, Post::getTitle, Post::getContent)
                .containsExactly(POST_ID, TITLE, CONTENT);
        }
    }

    @Nested
    class GetPostTest {

        @Test
        void givenExistingPost_whenGetPost_thenPostIsFound() {
            // given
            Post expectedPost = Post.builder()
                .id(POST_ID)
                .title(TITLE)
                .content(CONTENT)
                .authorId(USER.getId())
                .build();

            given(postRepository.findById(POST_ID)).willReturn(Optional.of(expectedPost));

            // when
            Post foundPost = postService.getPost(POST_ID);

            // then
            then(postRepository).should().findById(POST_ID);

            assertThat(foundPost)
                .extracting(Post::getId, Post::getTitle, Post::getContent)
                .containsExactly(POST_ID, TITLE, CONTENT);
        }

        @Test
        void givenNonExistentPost_whenGetPost_thenPostIsNotFound() {
            // given
            given(postRepository.findById(NON_EXISTENT_ID))
                .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> postService.getPost(NON_EXISTENT_ID))
                .isInstanceOf(PostException.class);

            then(postRepository).should().findById(NON_EXISTENT_ID);
        }
    }

    @Nested
    class UpdatePostTest {

        @Test
        void givenExistingPostAndValidPostRequest_whenUpdatePost_thenPostIsUpdated() {
            // given
            String updatedTitle = "updated title";
            String updatedContent = "updated content";
            UpsertPost request = new TestUpsertPost(updatedTitle, updatedContent);

            Post existingPost = Post.builder()
                .id(POST_ID)
                .title(TITLE)
                .content(CONTENT)
                .authorId(USER.getId())
                .build();

            Post savedPost = Post.builder()
                .id(POST_ID)
                .title(updatedTitle)
                .content(updatedContent)
                .authorId(USER.getId())
                .build();

            given(postRepository.findById(POST_ID)).willReturn(Optional.of(existingPost));
            given(postRepository.save(any(Post.class))).willReturn(savedPost);

            // when
            Post updatedPost = postService.updatePost(POST_ID, request, USER);

            // then
            then(postRepository).should().findById(POST_ID);
            then(postRepository).should().save(refEq(savedPost));

            assertThat(updatedPost)
                .extracting(Post::getId, Post::getTitle, Post::getContent)
                .containsExactly(POST_ID, updatedTitle, updatedContent);
        }

        @Test
        void givenNonExistentPostAndValidPostRequest_whenUpdatePost_thenPostIsNotFound() {
            // given
            UpsertPost request = new TestUpsertPost(TITLE, CONTENT);
            given(postRepository.findById(NON_EXISTENT_ID)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> postService.updatePost(NON_EXISTENT_ID, request, USER))
                .isInstanceOf(PostException.class);

            then(postRepository).should().findById(NON_EXISTENT_ID);
            then(postRepository).shouldHaveNoMoreInteractions();
        }
    }

    @Nested
    class DeletePostTest {

        @Test
        void givenPostId_whenDeletePost_thenPostIsDeleted() {
            // when
            postService.deletePost(POST_ID, USER);

            // then
            then(postRepository).should().deleteById(POST_ID);
        }
    }
}
