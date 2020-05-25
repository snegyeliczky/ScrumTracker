package com.codecool.scrumtracker.service;

import com.codecool.scrumtracker.model.Status;
import com.codecool.scrumtracker.model.Task;
import com.codecool.scrumtracker.model.credentials.TaskTransferCredentials;
import com.codecool.scrumtracker.repository.StatusRepository;
import com.codecool.scrumtracker.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class TaskService {

    @Autowired
    StatusRepository statusRepository;

    @Autowired
    TaskRepository taskRepository;

    public void transferTask(TaskTransferCredentials credentials) {

        Status fromStatus = statusRepository.findById(credentials.getFromStatusId()).get();
        Status toStatus = statusRepository.findById(credentials.getToStatusId()).get();
        Task task = taskRepository.findById(credentials.getTaskId()).get();
        fromStatus.getTasks().remove(task);
        toStatus.getTasks().add(task);
        statusRepository.save(fromStatus);
        statusRepository.save(toStatus);

    }
}
