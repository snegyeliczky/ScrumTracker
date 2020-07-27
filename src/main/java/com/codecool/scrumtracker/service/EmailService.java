package com.codecool.scrumtracker.service;

import com.codecool.scrumtracker.model.Project;
import com.codecool.scrumtracker.model.Status;
import com.codecool.scrumtracker.model.Task;
import com.codecool.scrumtracker.repository.ProjectRepository;
import com.codecool.scrumtracker.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
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

    public void sendEmailToAddress(UUID projectId, String emailAddress) throws MessagingException {
        Project project = projectRepository.findById(projectId).get();

        MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String htmlMsg = buildHtmlMessage(project);

        helper.setTo(emailAddress);
        helper.setText(htmlMsg, true);
        helper.setSubject("Project sprint plan");

        emailSender.send(message);

    }

    private String buildHtmlMessage(Project project) {

        Set<Status> statuses = project.getTable().getStatuses();        //get all statuses
        Set<Task> tasks = new HashSet<>();                              //initialize new Task set
        statuses.forEach(status -> {
            tasks.addAll(status.getTasks());
        });                                                             //fill set with project tasks

        StringBuilder msg = new StringBuilder();
        msg.append("<html><body>");
        msg.append(buildHtmlHeader(project));
        msg.append(buildHtmlTable(tasks));
        msg.append("</body></html>");
        return msg.toString();
    }

    private String buildHtmlHeader(Project project) {
        StringBuilder msg = new StringBuilder();
        msg.append("<h1>Project name: ").append(project.getTitle()).append("</h1>");
        msg.append("<h3>Project members:</h3>");
        msg.append("<ul>");
        msg.append("<li><a href = \"mailto:")
                .append(project.getAuthor().getEmail())
                .append("\">")
                .append(project.getAuthor().getUsername())
                .append("</a>")
                .append("</li>");
        project.getParticipants().forEach(member ->
                msg.append("<li><a href = \"mailto:")
                        .append(member.getEmail())
                        .append("\">")
                        .append(member.getUsername())
                        .append("</a>")
                        .append("</li>"));
        msg.append("</ul>");
        return msg.toString();
    }

    private String buildHtmlTable(Set<Task> tasks) {
        StringBuilder table = new StringBuilder();

        table.append("<table>");

        table.append(buildHtmlTableHeader());

        tasks.forEach(task -> {
            table.append(buildTaskRow(task));
        });

        table.append("</table>");

        return table.toString();
    }

    private String buildHtmlTableHeader() {
        StringBuilder header = new StringBuilder();
        header.append("<tr>");
        header.append("<th>Story Title</th>");
        header.append("<th>User Story</th>");
        header.append("<th>Acceptance Criteria</th>");
        header.append("<th>Business Value</th>");
        header.append("</tr>");
        return header.toString();
    }

    private String buildTaskRow(Task task) {
        StringBuilder row = new StringBuilder();
        row.append("<tr>");
        row.append("<td>" + task.getTitle() + "</td>");
        row.append("<td>" + task.getDescription() + "</td>");
        row.append("<td>" + task.getDeadline() + "</td>");
        row.append("<td>" + task.getPriority() + "</td>");
        row.append("</tr>");
        return row.toString();
    }

}