package com.codecool.scrumtracker.service;

import com.codecool.scrumtracker.exception.exceptions.NotAuthorizedException;
import com.codecool.scrumtracker.exception.exceptions.NotProjectOwnerException;
import com.codecool.scrumtracker.model.AppUser;
import com.codecool.scrumtracker.model.Project;
import com.codecool.scrumtracker.model.ScrumTable;
import com.codecool.scrumtracker.model.Status;
import com.codecool.scrumtracker.model.credentials.ProjectCredentials;
import com.codecool.scrumtracker.model.credentials.StatusCredentials;
import com.codecool.scrumtracker.model.credentials.TaskCredentials;
import com.codecool.scrumtracker.model.credentials.UserCredentials;
import com.codecool.scrumtracker.repository.*;
import com.codecool.scrumtracker.util.Util;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
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
    AppUserRepository appUserRepository;

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

        Assertions.assertThat(projectService.createNewProject(testProjectCredentials)
                .getTable()
                .getStatuses()
                .size()
        ).isEqualTo(3);

    }


    @Test
    public void testGetProjectByIdAuthorized() throws NotAuthorizedException {

        Project testProject = Project.builder()
                .archive(false)
                .title("test project")
                .build();

        when(util.projectAuthorization(any())).thenReturn(true);
        Mockito.when(projectRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(testProject));
        Assertions.assertThat(projectService.getProjectById(any())).isEqualTo(testProject);
    }

    @Test(expected = NotAuthorizedException.class)
    public void testGetProjectByIdNotAuthorized() throws NotAuthorizedException {

        Project testProject = Project.builder()
                .archive(false)
                .title("test project")
                .build();

        when(util.projectAuthorization(any())).thenReturn(false);
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

    @Test
    public void testAddUserToProject() {
        AppUser testUser = AppUser.builder()
                .username("testuser")
                .build();

        Set<AppUser> testParticipants = new HashSet<>();

        Project testProject = Project.builder()
                .title("test project")
                .participants(testParticipants)
                .build();

        UserCredentials testUserCredentials = UserCredentials.builder()
                .username("testuser")
                .build();

        Mockito.when(projectRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(testProject));
        Mockito.when(appUserRepository.findByUsername(any())).thenReturn(java.util.Optional.ofNullable(testUser));

        assertThat(projectService.addUserToProject(testProject.getId(), testUserCredentials)).contains(testUser);
    }

    @Test(expected = NotProjectOwnerException.class)
    public void testDeleteProjectByIdNotAuthorized() throws NotProjectOwnerException {
        Project testProject = Project.builder()
                .title("test project")
                .build();
        Mockito.when(projectRepository.findById(any())).thenReturn(Optional.ofNullable(testProject));
        when(util.checkUserIsProjectOwner(any())).thenReturn(false);

        projectService.deleteProjectById(testProject.getId());

    }

    @Test
    public void testGetMyProjects() {

        AppUser testUser = AppUser.builder()
                .username("testuser")
                .build();

        Project testProject = Project.builder()
                .title("test project")
                .build();

        Set<Project> projects = new HashSet<>();
        projects.add(testProject);
        Mockito.when(util.getUserFromContext()).thenReturn(testUser);
        Mockito.when(projectRepository.getByAuthorAndArchiveIsFalse(any())).thenReturn(projects);
        Mockito.when(projectRepository.findProjectByParticipantsContains(any())).thenReturn(projects);

        assertThat(projectService.getMyProjects()).contains(testProject);
    }

    @Test
    public void testGetMyProjectsWithoutArchive() {

        AppUser testUser = AppUser.builder()
                .username("testuser")
                .build();

        Project testProject = Project.builder()
                .title("test project")
                .build();

        Set<Project> projects = new HashSet<>();
        projects.add(testProject);
        Mockito.when(util.getUserFromContext()).thenReturn(testUser);
        Mockito.when(projectRepository.getByAuthorAndArchiveIsFalse(any())).thenReturn(projects);
        Mockito.when(projectRepository.findProjectsByParticipantsContainsAndAndArchiveIsFalse(any())).thenReturn(projects);

        assertThat(projectService.getMyProjectsWithoutArchive()).contains(testProject);
    }

    @Test
    public void testGetMyActiveProjects() {
        AppUser testUser = AppUser.builder()
                .username("testuser")
                .build();
        Project testProject = Project.builder()
                .title("test project")
                .build();
        Set<Project> projects = new HashSet<>();
        projects.add(testProject);

        Mockito.when(util.getUserFromContext()).thenReturn(testUser);
        Mockito.when(projectRepository.getByAuthorAndArchiveIsFalse(any())).thenReturn(projects);
        assertThat(projectService.getMyActiveProjects()).contains(testProject);
    }

    @Test
    public void testGetParticipateProjects() {
        AppUser testUser = AppUser.builder()
                .username("testuser")
                .build();
        Project testProject = Project.builder()
                .title("test project")
                .build();
        Set<Project> projects = new HashSet<>();
        projects.add(testProject);
        Mockito.when(util.getUserFromContext()).thenReturn(testUser);
        Mockito.when(projectRepository.findProjectsByParticipantsContainsAndAndArchiveIsFalse(any())).thenReturn(projects);
        assertThat(projectService.getParticipateProjects()).contains(testProject);

    }



}