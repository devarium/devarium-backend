package io.devarium.core.domain.project.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import io.devarium.core.domain.project.Project;
import io.devarium.core.domain.project.command.UpsertProject;
import io.devarium.core.domain.project.exception.ProjectErrorCode;
import io.devarium.core.domain.project.exception.ProjectException;
import io.devarium.core.domain.project.repository.ProjectRepository;
import io.devarium.core.domain.skill.Skill;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    private static final Long PROJECT_ID = 1L;
    private static final Long NON_EXISTENT_ID = 999L;
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final Set<Skill> SKILLS = Set.of(Skill.JAVA, Skill.SPRING_BOOT);

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectServiceImpl projectService;

    private record TestUpsertProject(
        String name,
        String description,
        Set<Skill> skills
    ) implements UpsertProject {

    }

    @Nested
    class CreateProject {

        @Test
        void givenValidProjectRequest_whenCreateProject_thenProjectIsSaved() {
            // given
            UpsertProject request = new TestUpsertProject(NAME, DESCRIPTION, SKILLS);

            Project expectedProject = Project.builder()
                .name(NAME)
                .description(DESCRIPTION)
                .skills(SKILLS)
                .build();

            Project savedProject = Project.builder()
                .id(PROJECT_ID)
                .name(NAME)
                .description(DESCRIPTION)
                .skills(SKILLS)
                .build();

            given(projectRepository.save(any(Project.class))).willReturn(savedProject);

            // when
            Project createdProject = projectService.createProject(request);

            // then
            then(projectRepository).should().save(refEq(expectedProject));

            assertThat(createdProject)
                .extracting(Project::getId, Project::getName, Project::getDescription,
                    Project::getSkills)
                .containsExactly(PROJECT_ID, NAME, DESCRIPTION, SKILLS);
        }
    }

    @Nested
    class GetProject {

        @Test
        void givenExistingProject_whenGetProject_thenProjectIsFound() {
            // given
            Project expectedProject = Project.builder()
                .id(PROJECT_ID)
                .name(NAME)
                .description(DESCRIPTION)
                .skills(SKILLS)
                .build();

            given(projectRepository.findById(PROJECT_ID)).willReturn(Optional.of(expectedProject));

            // when
            Project foundProject = projectService.getProject(PROJECT_ID);

            // then
            then(projectRepository).should().findById(PROJECT_ID);

            assertThat(foundProject)
                .extracting(Project::getId, Project::getName, Project::getDescription,
                    Project::getSkills)
                .containsExactly(PROJECT_ID, NAME, DESCRIPTION, SKILLS);
        }

        @Test
        void givenNonExistentProject_whenGetProject_thenProjectIsNotFound() {
            // given
            given(projectRepository.findById(NON_EXISTENT_ID))
                .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> projectService.getProject(NON_EXISTENT_ID))
                .isInstanceOf(ProjectException.class)
                .hasMessage(ProjectErrorCode.PROJECT_NOT_FOUND.getMessage(NON_EXISTENT_ID));

            then(projectRepository).should().findById(NON_EXISTENT_ID);
        }
    }

    @Nested
    class UpdateProject {

        @Test
        void givenExistingProjectAndValidProjectRequest_whenUpdateProject_thenProjectIsUpdated() {
            // given
            String updatedName = "updated name";
            String updatedDescription = "updated description";
            Set<Skill> updatedSkills = Set.of(Skill.PYTHON, Skill.DJANGO);
            UpsertProject request = new TestUpsertProject(
                updatedName,
                updatedDescription,
                updatedSkills
            );

            Project existingProject = Project.builder()
                .id(PROJECT_ID)
                .name(NAME)
                .description(DESCRIPTION)
                .skills(SKILLS)
                .build();

            Project savedProject = Project.builder()
                .id(PROJECT_ID)
                .name(updatedName)
                .description(updatedDescription)
                .skills(updatedSkills)
                .build();

            given(projectRepository.findById(PROJECT_ID)).willReturn(Optional.of(existingProject));
            given(projectRepository.save(any(Project.class))).willReturn(savedProject);

            // when
            Project updatedProject = projectService.updateProject(PROJECT_ID, request);

            // then
            then(projectRepository).should().findById(PROJECT_ID);
            then(projectRepository).should().save(refEq(savedProject));

            assertThat(updatedProject)
                .extracting(Project::getId, Project::getName, Project::getDescription,
                    Project::getSkills)
                .containsExactly(PROJECT_ID, updatedName, updatedDescription, updatedSkills);
        }

        @Test
        void givenNonExistentProjectAndValidProjectRequest_whenUpdateProject_thenProjectIsNotFound() {
            // given
            UpsertProject request = new TestUpsertProject(NAME, DESCRIPTION, SKILLS);
            given(projectRepository.findById(NON_EXISTENT_ID)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> projectService.updateProject(NON_EXISTENT_ID, request))
                .isInstanceOf(ProjectException.class)
                .hasMessage(ProjectErrorCode.PROJECT_NOT_FOUND.getMessage(NON_EXISTENT_ID));

            then(projectRepository).should().findById(NON_EXISTENT_ID);
            then(projectRepository).shouldHaveNoMoreInteractions();
        }
    }

    @Nested
    class DeleteProject {

        @Test
        void givenProjectId_whenDeleteProject_thenProjectIsDeleted() {
            // when
            projectService.deleteProject(PROJECT_ID);

            // then
            then(projectRepository).should().deleteById(PROJECT_ID);
        }
    }
}
