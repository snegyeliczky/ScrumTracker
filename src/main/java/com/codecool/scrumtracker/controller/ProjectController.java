package com.codecool.scrumtracker.controller;

import com.codecool.scrumtracker.model.AppUser;
import com.codecool.scrumtracker.model.Project;
import com.codecool.scrumtracker.model.ScrumTable;
import com.codecool.scrumtracker.model.Task;
import com.codecool.scrumtracker.model.credentials.StatusCredentials;
import com.codecool.scrumtracker.model.credentials.ProjectCredentials;
import com.codecool.scrumtracker.model.credentials.TaskCredentials;
import com.codecool.scrumtracker.model.credentials.UserCredentials;
import com.codecool.scrumtracker.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

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


    @GetMapping("/{id}")
    public Project getProjectById(@PathVariable UUID id) {
        return projectService.getProjectById(id);
    }

    @PostMapping("/newstatus")
    public ScrumTable addNewStatusToProject(@RequestBody StatusCredentials status) {
        return projectService.addNewStatusToProject(status);
    }

    @PostMapping("/newtask")
    public void addNewTask(@RequestBody TaskCredentials task) {
        projectService.addNewTask(task);
    }

    @DeleteMapping("/deletestatus")
    public void deleteStatusFromProject(@RequestParam(value = "statusid")UUID statusId,
                                        @RequestParam(value = "tableid")UUID tableId){
        projectService.deleteStatusFromProject(statusId,tableId);
    }

    @GetMapping("/gettable/{id}")
    public ScrumTable getScrumTableById(@PathVariable UUID id){
       return projectService.getScrumTableById(id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteProjectById(@PathVariable UUID id) {
        projectService.deleteProjectById(id);
    }

    @PutMapping("/archive/{id}")
    public void archiveProjectById(@PathVariable UUID id){
        projectService.archiveProjectById(id);
    }

    @PostMapping("/adduser/{projectId}")
    public Set<AppUser> addUserToProject(@PathVariable UUID projectId, @RequestBody UserCredentials userToAdd) {
        return projectService.addUserToProject(projectId, userToAdd);
    }

    @GetMapping("/getmyprojects")
    public Set<Project> getMyProjects() {
        return projectService.getMyProjectsWithoutArchive();
    }

    @GetMapping("/getmyprojectswitharchive")
    public Set<Project> getMyProjectsWithArchive(){
        return projectService.getMyProjects();
    };

    @GetMapping("/getactiveprojects")
    public Set<Project> getActiveProject(){
        return projectService.getMyActiveProjects();
    };

    @GetMapping("/getparticipateprojects")
    public Set<Project> getParticipateProjects(){
        return projectService.getParticipateProjects();
    }

    @GetMapping("/getarchiveprojects")
    public Set<Project> getArchiveProjects(){
        return projectService.geArchiveProjects();
    };

}
