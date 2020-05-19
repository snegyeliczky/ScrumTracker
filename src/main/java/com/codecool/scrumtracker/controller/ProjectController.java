package com.codecool.scrumtracker.controller;

import com.codecool.scrumtracker.model.Project;
import com.codecool.scrumtracker.model.credentials.ProjectCredentials;
import com.codecool.scrumtracker.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@CrossOrigin(allowCredentials = "true")
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @PostMapping("/create")
    public Project createNewProject(@RequestBody ProjectCredentials project) {
        return projectService.createNewProject(project);
    }

    @GetMapping("/getmyprojects")
    public Set<Project> getMyProjects() {
        return projectService.getMyProjects();
    }
}
