package com.codecool.scrumtracker.service;

import com.codecool.scrumtracker.model.Status;
import com.codecool.scrumtracker.model.Task;
import com.codecool.scrumtracker.model.credentials.TaskCredentials;
import com.codecool.scrumtracker.model.credentials.TaskTransferCredentials;
import com.codecool.scrumtracker.repository.StatusRepository;
import com.codecool.scrumtracker.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.UUID;

@Component
public class TaskService {

    @Autowired
    StatusRepository statusRepository;

    @Autowired
    TaskRepository taskRepository;

    public void changeTaskStatus(TaskTransferCredentials credentials) {

        Status fromStatus = statusRepository.findById(credentials.getFromStatusId()).get();
        Status toStatus = statusRepository.findById(credentials.getToStatusId()).get();
        Task task = taskRepository.findById(credentials.getTaskId()).get();
        fromStatus.getTasks().remove(task);
        toStatus.getTasks().add(task);
        statusRepository.save(fromStatus);
        statusRepository.save(toStatus);

    }

    public void deleteTaskById(UUID id) {
        Task taskToDelete = taskRepository.findById(id).get();
        Status status = statusRepository.findByTasksContains(taskToDelete).get();
        Set<Task> tasks = status.getTasks();
        tasks.remove(taskToDelete);
        status.setTasks(tasks);
        statusRepository.save(status);
        taskRepository.deleteById(id);

    }

    public void editTaskData(UUID taskId, TaskCredentials taskCredentials) throws IllegalAccessException, NoSuchFieldException {

        Task taskToEdit = taskRepository.findById(taskId).get();
        for (Field f : taskCredentials.getClass().getDeclaredFields()) {
            if (f.get(this) != null) {
                f.set(taskToEdit, f.get(this));
            }
        }
    }

}
