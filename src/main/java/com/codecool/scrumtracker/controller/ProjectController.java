package com.codecool.scrumtracker.controller;

import com.codecool.scrumtracker.model.Project;
import com.codecool.scrumtracker.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(allowCredentials = "true")
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @PostMapping("/create")
    public Project createNewProject(@RequestBody String projectName) {
        return projectService.createNewProject(projectName);
    }
}
