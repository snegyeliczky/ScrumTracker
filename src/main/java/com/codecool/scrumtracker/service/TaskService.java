package com.codecool.scrumtracker.service;

import com.codecool.scrumtracker.model.Status;
import com.codecool.scrumtracker.model.Task;
import com.codecool.scrumtracker.model.credentials.TaskCredentials;
import com.codecool.scrumtracker.model.credentials.TaskTransferCredentials;
import com.codecool.scrumtracker.repository.AppUserRepository;
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

    @Autowired
    AppUserRepository appUserRepository;

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

    public Task editTaskData(UUID taskId, Task taskCredentials) throws IllegalAccessException, NoSuchFieldException {

        if (taskCredentials.getOwner() != null) {
            taskCredentials.setOwner(appUserRepository.findById(taskCredentials.getOwner().getId()).get());
        }
        Task taskToEdit = taskRepository.findById(taskId).get();
        for (Field f : taskCredentials.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            if (f.get(taskCredentials) != null) {
                f.set(taskToEdit, f.get(taskCredentials));
            }
        }
        taskRepository.save(taskToEdit);
        return taskToEdit;

    }

    public void archiveById(UUID id) {
        Task task = taskRepository.findById(id).get();
        task.setArchive(!task.isArchive());
        taskRepository.save(task);
    }
}
