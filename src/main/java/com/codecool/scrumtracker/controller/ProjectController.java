package com.codecool.scrumtracker.controller;

import com.codecool.scrumtracker.exception.exceptions.NotAuthoritizedException;
import com.codecool.scrumtracker.exception.exceptions.NotProjectOwnerException;
import com.codecool.scrumtracker.model.AppUser;
import com.codecool.scrumtracker.model.Project;
import com.codecool.scrumtracker.model.ScrumTable;
import com.codecool.scrumtracker.model.credentials.*;
import com.codecool.scrumtracker.service.EmailService;
import com.codecool.scrumtracker.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.Set;
import java.util.UUID;

@RestController
@CrossOrigin(allowCredentials = "true")
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @Autowired
    EmailService emailService;

    @PostMapping("/create")
    public Project createNewProject(@RequestBody ProjectCredentials project) {
        return projectService.createNewProject(project);
    }

    @PostMapping("/rename/{id}/{name}")
    public void renameProject(@PathVariable UUID id, @PathVariable String name){
        projectService.renameProject(id,name);
    }


    @GetMapping("/{id}")
    public Project getProjectById(@PathVariable UUID id) throws NotAuthoritizedException {

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
    public void deleteStatusFromProject(@RequestParam(value = "statusid") UUID statusId,
                                        @RequestParam(value = "tableid") UUID tableId) {
        projectService.deleteStatusFromProject(statusId, tableId);
    }

    @GetMapping("/gettable/{id}")
    public ScrumTable getScrumTableById(@PathVariable UUID id) throws NotAuthoritizedException {
        return projectService.getScrumTableById(id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteProjectById(@PathVariable UUID id) throws NotProjectOwnerException {
        projectService.deleteProjectById(id);
    }

    @PutMapping("/archive/{id}")
    public void archiveProjectById(@PathVariable UUID id) {
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
    public Set<Project> getMyProjectsWithArchive() {
        return projectService.getMyProjects();
    }



    @GetMapping("/getactiveprojects")
    public Set<Project> getActiveProject() {
        return projectService.getMyActiveProjects();
    }


    @GetMapping("/getparticipateprojects")
    public Set<Project> getParticipateProjects() {
        return projectService.getParticipateProjects();
    }

    @GetMapping("/getarchiveprojects")
    public Set<Project> getArchiveProjects() {
        return projectService.geArchiveProjects();
    }



    @PostMapping("/email/{projectId}")
    public void sendEmail(@PathVariable UUID projectId, @RequestBody EmailCredentials emailAddress) throws MessagingException {
        emailService.sendEmailToAddress(projectId, emailAddress.getEmail());
    }

    @PutMapping("/table/limit")
    public void updateInProgressLimitValue(@RequestBody ScrumTableCredentials credentials) {
        projectService.updateInProgressLimitValue(credentials);
    }
}
