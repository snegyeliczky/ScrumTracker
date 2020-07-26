package com.codecool.scrumtracker.service;

import com.codecool.scrumtracker.exception.exceptions.NotAuthorizedException;
import com.codecool.scrumtracker.model.AppUser;
import com.codecool.scrumtracker.model.Project;
import com.codecool.scrumtracker.model.ScrumTable;
import com.codecool.scrumtracker.model.Status;
import com.codecool.scrumtracker.model.credentials.ProjectCredentials;
import com.codecool.scrumtracker.model.credentials.StatusCredentials;
import com.codecool.scrumtracker.model.credentials.TaskCredentials;
import com.codecool.scrumtracker.model.credentials.UserCredentials;
import com.codecool.scrumtracker.repository.ProjectRepository;
import com.codecool.scrumtracker.repository.ScrumTableRepository;
import com.codecool.scrumtracker.repository.StatusRepository;
import com.codecool.scrumtracker.repository.TaskRepository;
import com.codecool.scrumtracker.util.Util;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.*;


import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectServiceTest {

    @SpyBean
    private ProjectService projectService;

    @MockBean
    ProjectRepository projectRepository;

    @MockBean
    StatusRepository statusRepository;

    @MockBean
    ScrumTableRepository scrumTableRepository;

    @MockBean
    UserService userService;

    @MockBean
    TaskRepository taskRepository;

    @MockBean
    Util util;



    @Test
    public void testCreateNewProject() {

        ProjectCredentials testProjectCredentials = new ProjectCredentials("test project");

        AppUser testUser = AppUser.builder()
                .email("test@test.com")
                .username("testuser")
                .build();

        when(util.getUserFromContext()).thenReturn(testUser);

        doNothing().when(userService).newProject(any());

        Assertions.assertThat(projectService.createNewProject(testProjectCredentials)
                .getTable()
                .getStatuses()
                .size()
        ).isEqualTo(3);

    }


    @Test
    public void testGetProjectById() throws NotAuthorizedException {

        Project testProject = Project.builder()
                .archive(false)
                .title("test project")
                .build();


        when(util.projectAuthorization(any())).thenReturn(true);

        Mockito.when(projectRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(testProject));

        Assertions.assertThat(projectService.getProjectById(any())).isEqualTo(testProject);
    }

    @Test
    public void testAddNewStatusToProject() {

        Status toDo = Status.builder()
                .statusName("To Do")
                .position(1)
                .build();


        Set<Status> testStatuses = new HashSet<>(Arrays.asList(toDo));

        ScrumTable testTable = ScrumTable.builder()
                .statuses(testStatuses)
                .taskLimit(0)
                .build();

        Project testProject = Project.builder()
                .archive(false)
                .title("test project")
                .table(testTable)
                .build();

        StatusCredentials testCredentials = StatusCredentials.builder()
                .projectId(testProject.getId())
                .statusName("test status")
                .build();

        Mockito.when(projectRepository.findById(any())).thenReturn(java.util.Optional.of(testProject));

        assertThat(projectService.addNewStatusToProject(testCredentials)
                .getStatuses()
                .stream()
                .mapToInt(Status::getPosition)
                .max()
                .getAsInt()).isEqualTo(2);
    }

    @Test
    public void testGetScrumTableByIdAuthorized() throws NotAuthorizedException {
        ScrumTable testTable = ScrumTable.builder()
                .taskLimit(0)
                .build();
        Project testProject = Project.builder()
                .title("test project")
                .build();

        Mockito.when(scrumTableRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(testTable));
        Mockito.when(projectRepository.getProjectByTable(any())).thenReturn(java.util.Optional.ofNullable(testProject));
        when(util.projectAuthorization(any())).thenReturn(true);

        assertThat(projectService.getScrumTableById(testTable.getId())).isEqualTo(testTable);
    }

    @Test(expected = NotAuthorizedException.class)
    public void testGetScrumTableByIdNotAuthorized() throws NotAuthorizedException {
        ScrumTable testTable = ScrumTable.builder()
                .taskLimit(0)
                .build();
        Project testProject = Project.builder()
                .title("test project")
                .build();

        Mockito.when(scrumTableRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(testTable));
        Mockito.when(projectRepository.getProjectByTable(any())).thenReturn(java.util.Optional.ofNullable(testProject));
        when(util.projectAuthorization(any())).thenReturn(false);

        assertThat(projectService.getScrumTableById(testTable.getId())).isEqualTo(testTable);
    }



   /* @Test
    public void testDeleteStatusFromProject() {

        Status toDo = Status.builder()
                .statusName("To Do")
                .position(1)
                .build();

        Status done = Status.builder()
                .statusName("Done")
                .position(2)
                .build();


        Set<Status> testStatuses = new HashSet<>(Arrays.asList(toDo, done));

        ScrumTable testTable = ScrumTable.builder()
                .statuses(testStatuses)
                .taskLimit(0)
                .build();

        Mockito.when(scrumTableRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(testTable));
        Mockito.when(statusRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(toDo));

    }*/



    /*@Test
    public void testCreateNewProject() {

        ProjectCredentials credentials = new ProjectCredentials("test project");

        AppUser testUser = AppUser.builder()
                .email("test@test.com")
                .username("testUser")
                .projectsCount(0)
                .finishedTaskCount(0)
                .participantCount(0)
                .tasksCount(0)
                .build();

        doNothing().when(projectService).statusRepository.saveAll(any());
        doNothing().when(projectService).scrumTableRepository.save(any());
        doNothing().when(projectService).projectRepository.save(any());
        doNothing().when(projectService).userService.newProject(any());

        Project testProject = projectService.createNewProject(credentials);
        Mockito.when(util.getUserFromContext()).thenReturn(testUser);
        assertThat(projectService.createNewProject(credentials)).isEqualTo(testProject);
    }*/

}