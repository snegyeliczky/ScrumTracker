package com.codecool.scrumtracker.service;

import com.codecool.scrumtracker.model.Project;
import com.codecool.scrumtracker.model.Status;
import com.codecool.scrumtracker.model.Task;
import com.codecool.scrumtracker.repository.ProjectRepository;
import com.codecool.scrumtracker.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
public class EmailService /*implements EmailService*/ {

    @Autowired
    public JavaMailSender emailSender;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    TaskRepository taskRepository;

    public void sendSimpleMessage(String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);

    }

    public void sendEmailToAddress(UUID projectId, String emailAddress) {
        Project project = projectRepository.findById(projectId).get();
        Set<Status> projectStatuses = project.getTable().getStatuses();
        Set<Task> projectTasks = new HashSet<>();
        projectStatuses.forEach(status -> {
            projectTasks.addAll(status.getTasks());
        });
        StringBuilder email = new StringBuilder();
        projectTasks.forEach(task -> {
            email.append(task.getStoryTitle() + " " + task.getUserStory() + " ");
        });
        sendSimpleMessage(emailAddress, project.getTitle(), email.toString());
    }

}