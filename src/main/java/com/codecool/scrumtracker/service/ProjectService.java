package com.codecool.scrumtracker.service;

import com.codecool.scrumtracker.model.*;
import com.codecool.scrumtracker.model.credentials.ProjectCredentials;
import com.codecool.scrumtracker.model.credentials.StatusCredentials;
import com.codecool.scrumtracker.model.credentials.TaskCredentials;
import com.codecool.scrumtracker.repository.ProjectRepository;
import com.codecool.scrumtracker.repository.ScrumTableRepository;
import com.codecool.scrumtracker.repository.StatusRepository;
import com.codecool.scrumtracker.repository.TaskRepository;
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
                .build();
        return table;
    }

    public Project createNewProject(ProjectCredentials project) {
        AppUser user = util.getUserFromContext();

        Set<Status> initialStates = createBaseStatus();

        ScrumTable table = createScrumTable(initialStates);

        Project newProject = Project.builder()
                .table(table)
                .author(user)
                .title(project.getProjectName())
                .build();

        statusRepository.saveAll(initialStates);
        scrumTableRepository.save(table);
        return projectRepository.save(newProject);
    }

    public Status createStatus(String name, int position) {
        return Status.builder()
                .statusName(name)
                .position(position)
                .build();
    }

    public Set<Project> getMyProjects() {
        AppUser user = util.getUserFromContext();
        return projectRepository.getProjectByAuthor(user);
    }

    public Project getProjectById(UUID id) {
        return projectRepository.findById(id).get();
    }

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
        statuses.remove(status);

        scrumTable.setStatuses(statuses);
        scrumTableRepository.save(scrumTable);
        statusRepository.deleteById(statusId);

        deleteTasksFromDatabase(status.getTasks());
    }

    public ScrumTable getScrumTableById(UUID id) {
        return scrumTableRepository.findById(id).get();
    }

    private void deleteTasksFromDatabase(Set<Task> tasks) {
        tasks.forEach(task -> taskRepository.deleteById(task.getId()));
    }

    public void deleteProjectById(UUID id) {

        projectRepository.deleteById(id);
    }
}
