package com.codecool.scrumtracker.service;

import com.codecool.scrumtracker.exception.exceptions.NotAuthorizedException;
import com.codecool.scrumtracker.exception.exceptions.NotProjectOwnerException;
import com.codecool.scrumtracker.model.*;
import com.codecool.scrumtracker.model.credentials.*;
import com.codecool.scrumtracker.repository.*;
import com.codecool.scrumtracker.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ProjectService {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    Util util;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    StatusRepository statusRepository;

    @Autowired
    ScrumTableRepository scrumTableRepository;

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    UserService userService;

    private Set<Status> createBaseStatus() {
        Status toDo = createStatus("To Do", 1);
        Status inProgress = createStatus("In Progress", 2);
        Status done = createStatus("Done", 3);
        Set<Status> initialStatuses = new HashSet<>(Arrays.asList(toDo, inProgress, done));
        return initialStatuses;
    }

    private ScrumTable createScrumTable(Set<Status> initialStatuses) {
        ScrumTable table = ScrumTable.builder()
                .statuses(initialStatuses)
                .taskLimit(0)
                .build();
        return table;
    }

    //Test already added
    public Project createNewProject(ProjectCredentials project) {
        AppUser user = util.getUserFromContext();

        Set<Status> initialStates = createBaseStatus();

        ScrumTable table = createScrumTable(initialStates);

        Project newProject = Project.builder()
                .table(table)
                .author(user)
                .title(project.getProjectName())
                .archive(false)
                .build();

        userService.newProject(user);
        statusRepository.saveAll(initialStates);
        scrumTableRepository.save(table);
        projectRepository.save(newProject);
        return newProject;
    }

    private Status createStatus(String name, int position) {
        return Status.builder()
                .statusName(name)
                .position(position)
                .build();
    }

    //Test already added
    public Project getProjectById(UUID id) throws NotAuthorizedException {
        Project project = projectRepository.findById(id).get();
        if (!util.projectAuthorization(project)) {
            throw new NotAuthorizedException("You don't have permission", new Throwable());
        }
        return project;
    }

    //Test already added
    public ScrumTable addNewStatusToProject(StatusCredentials status) {

        Project project = projectRepository.findById(status.getProjectId()).get();
        ScrumTable table = project.getTable();
        int max = table.getStatuses()
                .stream()
                .mapToInt(Status::getPosition)
                .max()
                .orElse(0);
        Status newStatus = createStatus(status.getStatusName(), max + 1);
        Set<Status> projectStatuses = table.getStatuses();

        projectStatuses.add(newStatus);
        table.setStatuses(projectStatuses);
        project.setTable(table);
        statusRepository.save(newStatus);
        scrumTableRepository.save(table);
        projectRepository.save(project);
        return table;

    }

    public void addNewTask(TaskCredentials taskCredentials) {
        AppUser user = util.getUserFromContext();
        Status status = statusRepository.findById(taskCredentials.getStatusId()).get();
        int newPosition = status.getTasks()
                .stream()
                .mapToInt(Task::getPosition)
                .max()
                .orElse(0);
        Task builtTask = Task.builder()
                .author(user)
                .title(taskCredentials.getTitle())
                .position(newPosition + 1)
                .finished(false)
                .priority(0)
                .build();
        Set<Task> taskSet = status.getTasks();
        taskSet.add(builtTask);
        status.setTasks(taskSet);
        taskRepository.save(builtTask);
        statusRepository.save(status);


    }

    public void deleteStatusFromProject(UUID statusId, UUID tableId) {

        ScrumTable scrumTable = scrumTableRepository.findById(tableId).get();
        Status status = statusRepository.findById(statusId).get();

        Set<Status> statuses = scrumTable.getStatuses();
        int position = status.getPosition();
        statuses.remove(status);

        reArrangeStatusPositions(statuses, position);

        scrumTable.setStatuses(statuses);
        scrumTableRepository.save(scrumTable);
        statusRepository.deleteById(statusId);

        deleteTasksFromDatabase(status.getTasks());
    }

    private void reArrangeStatusPositions(Set<Status> statuses, int position) {

        statuses.forEach(status -> {
            if (status.getPosition() > position) {
                status.setPosition(status.getPosition() - 1);
                statusRepository.save(status);
            }
        });

    }

    //Test already added
    public ScrumTable getScrumTableById(UUID id) throws NotAuthorizedException {
        ScrumTable table = scrumTableRepository.findById(id).get();
        Project project = projectRepository.getProjectByTable(table).get();
        if (!util.projectAuthorization(project)) {
            throw new NotAuthorizedException("You don't have permission", new Throwable());
        }
        return table;
    }

    private void deleteTasksFromDatabase(Set<Task> tasks) {
        tasks.forEach(task -> taskRepository.deleteById(task.getId()));
    }

    public void deleteProjectById(UUID id) throws NotProjectOwnerException {
        Project project = projectRepository.findById(id).get();
        if (!util.checkUserIsProjectOwner(project)) {
            throw new NotProjectOwnerException("You are not the project owner", new Throwable());
        }
        projectRepository.deleteById(id);
    }

    //Test already added
    public Set<AppUser> addUserToProject(UUID projectId, UserCredentials userToAdd) {

        Project project = projectRepository.findById(projectId).get();
        AppUser user = appUserRepository.findByUsername(userToAdd.getUsername()).get();
        Set<AppUser> participants = project.getParticipants();
        userService.incrementParticipantCount(user);
        participants.add(user);
        project.setParticipants(participants);
        projectRepository.save(project);
        return participants;

    }

    public void archiveProjectById(UUID id) {
        Project project = projectRepository.findById(id).get();
        project.setArchive(!project.isArchive());
        projectRepository.save(project);
    }

    public Set<Project> getMyProjects() {
        AppUser user = util.getUserFromContext();
        Set<Project> projects = projectRepository.getProjectByAuthor(user);
        Set<Project> participant = projectRepository.findProjectByParticipantsContains(user);
        projects.addAll(participant);
        return projects;
    }

    public Set<Project> getMyProjectsWithoutArchive() {
        AppUser user = util.getUserFromContext();
        Set<Project> projects = projectRepository.getByAuthorAndArchiveIsFalse(user);
        Set<Project> participate = projectRepository.findProjectsByParticipantsContainsAndAndArchiveIsFalse(user);
        projects.addAll(participate);
        return projects;
    };

    public Set<Project> getMyActiveProjects() {
        AppUser user = util.getUserFromContext();
        return projectRepository.getByAuthorAndArchiveIsFalse(user);
    }

    public Set<Project> getParticipateProjects() {
        AppUser user = util.getUserFromContext();
        Set<Project> projectsByParticipantsContainsAndAndArchiveIsFalse = projectRepository.findProjectsByParticipantsContainsAndAndArchiveIsFalse(user);
        return projectRepository.findProjectsByParticipantsContainsAndAndArchiveIsFalse(user);
    }

    public Set<Project> geArchiveProjects() {
        AppUser user = util.getUserFromContext();
        Set<Project> projects = projectRepository.getProjectByAuthorAndArchiveIsTrue(user);
        Set<Project> participants = projectRepository.getProjectByParticipantsContainsAndArchiveIsTrue(user);
        projects.addAll(participants);
        return projects;
    }

    public void updateInProgressLimitValue(ScrumTableCredentials credentials) {
        ScrumTable table = scrumTableRepository.findById(credentials.getId()).get();
        table.setTaskLimit(credentials.getTaskLimit());
        scrumTableRepository.save(table);
    }
}
