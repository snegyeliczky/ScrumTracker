package com.codecool.scrumtracker.service;

import com.codecool.scrumtracker.model.AppUser;
import com.codecool.scrumtracker.model.Project;
import com.codecool.scrumtracker.model.ScrumTable;
import com.codecool.scrumtracker.model.Status;
import com.codecool.scrumtracker.model.credentials.ProjectCredentials;
import com.codecool.scrumtracker.repository.ProjectRepository;
import com.codecool.scrumtracker.repository.ScrumTableRepository;
import com.codecool.scrumtracker.repository.StatusRepository;
import com.codecool.scrumtracker.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
public class ProjectService {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    Util util;

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
}
