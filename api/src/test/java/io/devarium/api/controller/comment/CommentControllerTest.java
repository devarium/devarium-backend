package io.devarium.api.controller.comment;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.devarium.api.util.MockMvcTestUtils;
import io.devarium.infrastructure.persistence.entity.CommentEntity;
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
class CommentControllerTest {

    private static final String BASE_URL = "/api/v1/comments";
    private static final Long NON_EXISTENT_ID = 999L;
    private static final String CONTENT = "content";
    private static final String REQUEST_BODY = """
        {
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

        assert em.createQuery("SELECT COUNT(p) FROM CommentEntity p", Long.class)
            .getSingleResult() == 0;
    }

    @AfterEach
    void tearDown() {
        withTransaction(em -> {
            em.createQuery("DELETE FROM CommentEntity").executeUpdate();
            em.createNativeQuery("ALTER TABLE comments ALTER COLUMN id RESTART WITH 1")
                .executeUpdate();
        });
        em.close();
    }

    private CommentEntity createTestComment() {
        CommentEntity comment = CommentEntity.builder()
            .content(CONTENT)
            .build();

        withTransaction(em -> em.persist(comment));
        return comment;
    }

    @Nested
    class CreateComment {

        @Test
        void givenValidRequest_whenCreateComment_thenSuccess() {
            // when & then
            MockMvcTestUtils.performPost(
                mockMvc,
                BASE_URL,
                REQUEST_BODY.formatted(CONTENT),
                status().isCreated(),
                jsonPath("$.data.commentId").exists(),
                jsonPath("$.data.content").value(CONTENT)
            );
        }

        @Test
        void givenInvalidRequest_whenCreateComment_thenBadRequest() {
            // when & then
            MockMvcTestUtils.performPost(
                mockMvc,
                BASE_URL,
                REQUEST_BODY.formatted(""),
                status().isBadRequest()
            );
        }
    }


    @Nested
    class GetComment {

        @Test
        void givenExistingId_whenGetComment_thenSuccess() {
            // given
            CommentEntity comment = createTestComment();

            // when & then
            MockMvcTestUtils.performGet(
                mockMvc,
                BASE_URL + "/" + comment.getId(),
                status().isOk(),
                jsonPath("$.data.commentId").value(comment.getId()),
                jsonPath("$.data.content").value(CONTENT)
            );
        }

        @Test
        void givenNonExistentId_whenGetComment_thenNotFound() {
            // when & then
            MockMvcTestUtils.performGet(
                mockMvc,
                BASE_URL + "/" + NON_EXISTENT_ID,
                status().isNotFound()
            );
        }
    }

    @Nested
    class UpdateComment {

        @Test
        void givenExistingIdAndValidRequest_whenUpdateComment_thenSuccess() {
            // given
            CommentEntity comment = createTestComment();
            String updatedContent = "updated content";

            // when & then
            MockMvcTestUtils.performPut(
                mockMvc,
                BASE_URL + "/" + comment.getId(),
                REQUEST_BODY.formatted(updatedContent),
                status().isOk(),
                jsonPath("$.data.commentId").value(comment.getId()),
                jsonPath("$.data.content").value(updatedContent)
            );
        }

        @Test
        void givenNonExistentIdAndValidRequest_whenUpdateComment_thenNotFound() {
            // when & then
            MockMvcTestUtils.performPut(
                mockMvc,
                BASE_URL + "/" + NON_EXISTENT_ID,
                REQUEST_BODY.formatted(CONTENT),
                status().isNotFound()
            );
        }
    }

    @Nested
    class DeleteComment {

        @Test
        void givenExistingId_whenDeleteComment_thenSuccess() {
            // given
            CommentEntity comment = createTestComment();

            // when & then
            MockMvcTestUtils.performDelete(
                mockMvc,
                BASE_URL + "/" + comment.getId(),
                status().isNoContent()
            );
        }
    }
}
