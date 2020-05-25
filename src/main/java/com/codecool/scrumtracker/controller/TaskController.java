package com.codecool.scrumtracker.controller;

import com.codecool.scrumtracker.model.credentials.TaskTransferCredentials;
import com.codecool.scrumtracker.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(allowCredentials = "true")
@RequestMapping("/task")
public class TaskController {

    @Autowired
    TaskService taskService;

    @PutMapping("/transfer")
    public void transferTask(@RequestBody TaskTransferCredentials credentials) {
        taskService.transferTask(credentials);
    }

}
