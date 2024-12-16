package io.devarium.api.controller.project;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.devarium.api.util.MockMvcTestUtils;
import io.devarium.core.domain.skill.Skill;
import io.devarium.infrastructure.persistence.entity.ProjectEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.Set;
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
class ProjectControllerTest {

    private static final String BASE_URL = "/api/v1/projects";
    private static final Long NON_EXISTENT_ID = 999L;
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final Set<Skill> SKILLS = Set.of(Skill.JAVA, Skill.SPRING_BOOT);
    private static final String REQUEST_BODY = """
        {
            "name": "%s",
            "description": "%s",
            "skills": ["JAVA", "SPRING_BOOT"]
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

        assert em.createQuery("SELECT COUNT(p) FROM ProjectEntity p", Long.class)
            .getSingleResult() == 0;
    }

    @AfterEach
    void tearDown() {
        withTransaction(em -> {
            em.createQuery("DELETE FROM ProjectEntity").executeUpdate();
            em.createNativeQuery("ALTER TABLE projects ALTER COLUMN id RESTART WITH 1")
                .executeUpdate();
        });
        em.close();
    }

    private ProjectEntity createTestProject() {
        ProjectEntity project = ProjectEntity.builder()
            .name(NAME)
            .description(DESCRIPTION)
            .skills(SKILLS)
            .build();

        withTransaction(em -> em.persist(project));
        return project;
    }

    @Nested
    class CreateProject {

        @Test
        void givenValidRequest_whenCreateProject_thenSuccess() {
            // when & then
            MockMvcTestUtils.performPost(
                mockMvc,
                BASE_URL,
                REQUEST_BODY.formatted(NAME, DESCRIPTION),
                status().isCreated(),
                jsonPath("$.data.projectId").exists(),
                jsonPath("$.data.name").value(NAME),
                jsonPath("$.data.description").value(DESCRIPTION),
                jsonPath("$.data.skills").isArray(),
                jsonPath("$.data.skills.length()").value(2),
                jsonPath("$.data.skills[?(@=='JAVA')]").value("JAVA"),
                jsonPath("$.data.skills[?(@=='SPRING_BOOT')]").value("SPRING_BOOT")
            );
        }

        @Test
        void givenInvalidRequest_whenCreateProject_thenBadRequest() {
            // when & then
            MockMvcTestUtils.performPost(
                mockMvc,
                BASE_URL,
                REQUEST_BODY.formatted("", DESCRIPTION),
                status().isBadRequest()
            );
        }
    }

    @Nested
    class GetProject {

        @Test
        void givenExistingId_whenGetProject_thenSuccess() {
            // given
            ProjectEntity project = createTestProject();

            // when & then
            MockMvcTestUtils.performGet(
                mockMvc,
                BASE_URL + "/" + project.getId(),
                status().isOk(),
                jsonPath("$.data.projectId").value(project.getId()),
                jsonPath("$.data.name").value(NAME),
                jsonPath("$.data.description").value(DESCRIPTION),
                jsonPath("$.data.skills").isArray(),
                jsonPath("$.data.skills.length()").value(2),
                jsonPath("$.data.skills[?(@=='JAVA')]").value("JAVA"),
                jsonPath("$.data.skills[?(@=='SPRING_BOOT')]").value("SPRING_BOOT")
            );
        }

        @Test
        void givenNonExistentId_whenGetProject_thenNotFound() {
            // when & then
            MockMvcTestUtils.performGet(
                mockMvc,
                BASE_URL + "/" + NON_EXISTENT_ID,
                status().isNotFound()
            );
        }
    }

    @Nested
    class UpdateProject {

        @Test
        void givenExistingIdAndValidRequest_whenUpdateProject_thenSuccess() {
            // given
            ProjectEntity project = createTestProject();
            String updatedName = "updated project";
            String updatedDescription = "updated description";
            String updateRequestBody = """
                {
                    "name": "%s",
                    "description": "%s",
                    "skills": ["PYTHON", "DJANGO"]
                }
                """;

            // when & then
            MockMvcTestUtils.performPut(
                mockMvc,
                BASE_URL + "/" + project.getId(),
                updateRequestBody.formatted(updatedName, updatedDescription),
                status().isOk(),
                jsonPath("$.data.projectId").value(project.getId()),
                jsonPath("$.data.name").value(updatedName),
                jsonPath("$.data.description").value(updatedDescription),
                jsonPath("$.data.skills").isArray(),
                jsonPath("$.data.skills.length()").value(2),
                jsonPath("$.data.skills[?(@=='PYTHON')]").value("PYTHON"),
                jsonPath("$.data.skills[?(@=='DJANGO')]").value("DJANGO")
            );
        }

        @Test
        void givenNonExistentIdAndValidRequest_whenUpdateProject_thenNotFound() {
            // when & then
            MockMvcTestUtils.performPut(
                mockMvc,
                BASE_URL + "/" + NON_EXISTENT_ID,
                REQUEST_BODY.formatted(NAME, DESCRIPTION),
                status().isNotFound()
            );
        }

        @Test
        void givenExistingIdAndInvalidRequest_whenUpdateProject_thenBadRequest() {
            // given
            ProjectEntity project = createTestProject();

            // when & then
            MockMvcTestUtils.performPut(
                mockMvc,
                BASE_URL + "/" + project.getId(),
                REQUEST_BODY.formatted("", DESCRIPTION),
                status().isBadRequest()
            );
        }
    }

    @Nested
    class DeleteProject {

        @Test
        void givenExistingId_whenDeleteProject_thenSuccess() {
            // given
            ProjectEntity project = createTestProject();

            // when & then
            MockMvcTestUtils.performDelete(
                mockMvc,
                BASE_URL + "/" + project.getId(),
                status().isNoContent()
            );
        }
    }
}
