package io.devarium.api.controller.reply;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.devarium.api.util.MockMvcTestUtils;
import io.devarium.infrastructure.persistence.entity.ReplyEntity;
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
class ReplyControllerTest {

    private static final String BASE_URL = "/api/v1/replies";
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

        assert em.createQuery("SELECT COUNT(p) FROM ReplyEntity p", Long.class)
            .getSingleResult() == 0;
    }

    @AfterEach
    void tearDown() {
        withTransaction(em -> {
            em.createQuery("DELETE FROM ReplyEntity").executeUpdate();
            em.createNativeQuery("ALTER TABLE replies ALTER COLUMN id RESTART WITH 1")
                .executeUpdate();
        });
        em.close();
    }

    private ReplyEntity createTestReply() {
        ReplyEntity reply = ReplyEntity.builder()
            .content(CONTENT)
            .build();

        withTransaction(em -> em.persist(reply));
        return reply;
    }

    @Nested
    class CreateReply {

        @Test
        void givenValidRequest_whenCreateReply_thenSuccess() {
            // when & then
            MockMvcTestUtils.performPost(
                mockMvc,
                BASE_URL,
                REQUEST_BODY.formatted(CONTENT),
                status().isCreated(),
                jsonPath("$.data.replyId").exists(),
                jsonPath("$.data.content").value(CONTENT)
            );
        }

        @Test
        void givenInvalidRequest_whenCreateReply_thenBadRequest() {
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
    class GetReply {

        @Test
        void givenExistingId_whenGetReply_thenSuccess() {
            // given
            ReplyEntity reply = createTestReply();

            // when & then
            MockMvcTestUtils.performGet(
                mockMvc,
                BASE_URL + "/" + reply.getId(),
                status().isOk(),
                jsonPath("$.data.replyId").value(reply.getId()),
                jsonPath("$.data.content").value(CONTENT)
            );
        }

        @Test
        void givenNonExistentId_whenGetReply_thenNotFound() {
            // when & then
            MockMvcTestUtils.performGet(
                mockMvc,
                BASE_URL + "/" + NON_EXISTENT_ID,
                status().isNotFound()
            );
        }
    }

    @Nested
    class UpdateReply {

        @Test
        void givenExistingIdAndValidRequest_whenUpdateReply_thenSuccess() {
            // given
            ReplyEntity reply = createTestReply();
            String updatedContent = "updated content";

            // when & then
            MockMvcTestUtils.performPut(
                mockMvc,
                BASE_URL + "/" + reply.getId(),
                REQUEST_BODY.formatted(updatedContent),
                status().isOk(),
                jsonPath("$.data.replyId").value(reply.getId()),
                jsonPath("$.data.content").value(updatedContent)
            );
        }

        @Test
        void givenNonExistentIdAndValidRequest_whenUpdateReply_thenNotFound() {
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
    class DeleteReply {

        @Test
        void givenExistingId_whenDeleteReply_thenSuccess() {
            // given
            ReplyEntity reply = createTestReply();

            // when & then
            MockMvcTestUtils.performDelete(
                mockMvc,
                BASE_URL + "/" + reply.getId(),
                status().isNoContent()
            );
        }
    }
}
