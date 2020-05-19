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

    public Project createNewProject(ProjectCredentials project) {

        AppUser user = util.getUserFromContext();

        Status toDo = createStatus("To Do", 1);
        Status inProgress = createStatus("In Progress", 2);
        Status done = createStatus("Done", 3);

        Set<Status> initialStatuses = new HashSet<>(Arrays.asList(toDo, inProgress, done));

        ScrumTable table = ScrumTable.builder()
                .statuses(initialStatuses)
                .build();

        Project newProject = Project.builder()
                .table(table)
                .author(user)
                .title(project.getProjectName())
                .build();

        statusRepository.saveAll(initialStatuses);
        scrumTableRepository.save(table);
        projectRepository.save(newProject);
        return newProject;

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
}
