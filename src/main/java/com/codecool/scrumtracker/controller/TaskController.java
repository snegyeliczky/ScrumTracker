package com.codecool.scrumtracker.controller;

import com.codecool.scrumtracker.model.Task;
import com.codecool.scrumtracker.model.credentials.TaskCredentials;
import com.codecool.scrumtracker.model.credentials.TaskTransferCredentials;
import com.codecool.scrumtracker.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin(allowCredentials = "true")
@RequestMapping("/task")
public class TaskController {

    @Autowired
    TaskService taskService;

    @PutMapping("/transfer")
    public void changeTaskStatus(@RequestBody TaskTransferCredentials credentials) {
        taskService.changeTaskStatus(credentials);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteTaskById(@PathVariable UUID id) {
        taskService.deleteTaskById(id);
    }

    @PutMapping("/edit/{taskId}")
    public Task editTaskData(@PathVariable UUID taskId,
                             @RequestBody Task taskCredentials) throws NoSuchFieldException, IllegalAccessException {
        return taskService.editTaskData(taskId, taskCredentials);
    }
}
