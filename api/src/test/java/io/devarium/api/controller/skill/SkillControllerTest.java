package io.devarium.api.controller.skill;

import static io.devarium.api.util.MockMvcTestUtils.performGet;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.devarium.core.domain.skill.Skill;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class SkillControllerTest {

    private static final String BASE_URL = "/api/v1/skills";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenGetSkills_thenSuccess() {
        // when & then
        performGet(
            mockMvc,
            BASE_URL,
            status().isOk(),
            jsonPath("$.data").isArray(),
            jsonPath("$.data.length()").value(Skill.values().length),
            jsonPath("$.data[?(@.name=='JAVA')].displayName").value("Java"),
            jsonPath("$.data[?(@.name=='PYTHON')].displayName").value("Python"),
            jsonPath("$.data[?(@.name=='SPRING_BOOT')].displayName").value("Spring Boot"),
            jsonPath("$.data[?(@.name=='NESTJS')].displayName").value("NestJS"),
            jsonPath("$.data[?(@.name=='REACT')].displayName").value("React"),
            jsonPath("$.data[?(@.name=='ANGULAR')].displayName").value("Angular"),
            jsonPath("$.data[?(@.name=='POSTGRESQL')].displayName").value("PostgreSQL"),
            jsonPath("$.data[?(@.name=='MONGODB')].displayName").value("MongoDB"),
            jsonPath("$.data[?(@.name=='DOCKER')].displayName").value("Docker"),
            jsonPath("$.data[?(@.name=='KUBERNETES')].displayName").value("Kubernetes")
        );
    }
}
