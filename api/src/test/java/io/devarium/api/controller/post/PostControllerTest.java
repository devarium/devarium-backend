package io.devarium.api.controller.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.devarium.api.util.MockMvcTestUtils;
import io.devarium.infrastructure.persistence.entity.PostEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.function.Consumer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class PostControllerTest {

    private static final String BASE_URL = "/api/v1/posts";
    private static final Long NON_EXISTENT_ID = 999L;
    private static final String TITLE = "title";
    private static final String CONTENT = "content";
    private static final String REQUEST_BODY = """
        {
            "title": "%s",
            "content": "%s"
        }
        """;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManagerFactory emf;

    private EntityManager em;

    private void withTransaction(Consumer<EntityManager> block) {
        em.getTransaction().begin();
        block.accept(em);
        em.flush();
        em.getTransaction().commit();
    }

    @BeforeEach
    void setUp() {
        em = emf.createEntityManager();

        assert em.createQuery("SELECT COUNT(p) FROM PostEntity p", Long.class)
            .getSingleResult() == 0;
    }

    @AfterEach
    void tearDown() {
        withTransaction(em -> {
            em.createQuery("DELETE FROM PostEntity").executeUpdate();
            em.createNativeQuery("ALTER TABLE posts ALTER COLUMN id RESTART WITH 1")
                .executeUpdate();
        });
        em.close();
    }

    private PostEntity createTestPost() {
        PostEntity post = PostEntity.builder()
            .title(TITLE)
            .content(CONTENT)
            .build();

        withTransaction(em -> em.persist(post));
        return post;
    }

    @Nested
    class CreatePost {

        @Test
        void givenValidRequest_whenCreatePost_thenSuccess() {
            // when & then
            MockMvcTestUtils.performPost(
                mockMvc,
                BASE_URL,
                REQUEST_BODY.formatted(TITLE, CONTENT),
                status().isCreated(),
                jsonPath("$.data.postId").exists(),
                jsonPath("$.data.title").value(TITLE),
                jsonPath("$.data.content").value(CONTENT)
            );
        }

        @Test
        void givenInvalidRequest_whenCreatePost_thenBadRequest() {
            // when & then
            MockMvcTestUtils.performPost(
                mockMvc,
                BASE_URL,
                REQUEST_BODY.formatted(TITLE, ""),
                status().isBadRequest()
            );
        }
    }


    @Nested
    class GetPost {

        @Test
        void givenExistingId_whenGetPost_thenSuccess() {
            // given
            PostEntity post = createTestPost();

            // when & then
            MockMvcTestUtils.performGet(
                mockMvc,
                BASE_URL + "/" + post.getId(),
                status().isOk(),
                jsonPath("$.data.postId").value(post.getId()),
                jsonPath("$.data.title").value(TITLE),
                jsonPath("$.data.content").value(CONTENT)
            );
        }

        @Test
        void givenNonExistentId_whenGetPost_thenNotFound() {
            // when & then
            MockMvcTestUtils.performGet(
                mockMvc,
                BASE_URL + "/" + NON_EXISTENT_ID,
                status().isNotFound()
            );
        }
    }

    @Nested
    class UpdatePost {

        @Test
        void givenExistingIdAndValidRequest_whenUpdatePost_thenSuccess() {
            // given
            PostEntity post = createTestPost();
            String updatedTitle = "updated title";
            String updatedContent = "updated content";

            // when & then
            MockMvcTestUtils.performPut(
                mockMvc,
                BASE_URL + "/" + post.getId(),
                REQUEST_BODY.formatted(updatedTitle, updatedContent),
                status().isOk(),
                jsonPath("$.data.postId").value(post.getId()),
                jsonPath("$.data.title").value(updatedTitle),
                jsonPath("$.data.content").value(updatedContent)
            );
        }

        @Test
        void givenNonExistentIdAndValidRequest_whenUpdatePost_thenNotFound() {
            // when & then
            MockMvcTestUtils.performPut(
                mockMvc,
                BASE_URL + "/" + NON_EXISTENT_ID,
                REQUEST_BODY.formatted(TITLE, CONTENT),
                status().isNotFound()
            );
        }
    }

    @Nested
    class DeletePost {

        @Test
        void givenExistingId_whenDeletePost_thenSuccess() {
            // given
            PostEntity post = createTestPost();

            // when & then
            MockMvcTestUtils.performDelete(
                mockMvc,
                BASE_URL + "/" + post.getId(),
                status().isNoContent()
            );
        }
    }
}
