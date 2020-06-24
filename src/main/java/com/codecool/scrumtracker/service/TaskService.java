package com.codecool.scrumtracker.service;

import com.codecool.scrumtracker.exception.exceptions.ReachMaximumNumberOfTasksException;
import com.codecool.scrumtracker.model.AppUser;
import com.codecool.scrumtracker.model.ScrumTable;
import com.codecool.scrumtracker.model.Status;
import com.codecool.scrumtracker.model.Task;
import com.codecool.scrumtracker.model.credentials.TaskTransferCredentials;
import com.codecool.scrumtracker.repository.AppUserRepository;
import com.codecool.scrumtracker.repository.ScrumTableRepository;
import com.codecool.scrumtracker.repository.StatusRepository;
import com.codecool.scrumtracker.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class TaskService {

    @Autowired
    StatusRepository statusRepository;

    @Autowired
    UserService userService;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    ScrumTableRepository scrumTableRepository;

    public void changeTaskStatus(TaskTransferCredentials credentials) throws Exception {

        Status fromStatus = statusRepository.findById(credentials.getFromStatusId()).get();
        Status toStatus = statusRepository.findById(credentials.getToStatusId()).get();
        ScrumTable table = scrumTableRepository.findByStatusesContaining(fromStatus).get();
        if (checkScrumTableLimit(table, toStatus.getPosition())) {
            Task task = taskRepository.findById(credentials.getTaskId()).get();
            fromStatus.getTasks().remove(task);
            toStatus.getTasks().add(task);
            statusRepository.save(fromStatus);
            statusRepository.save(toStatus);
        } else {
            throw new ReachMaximumNumberOfTasksException("You reach the maximum number of tasks.", new Throwable());
        }

    }

    private boolean checkScrumTableLimit(ScrumTable table, Integer statusPosition) {
        Comparator<Status> comparator = Comparator.comparing( Status::getPosition );
        Status max = table.getStatuses().stream().max(comparator).get();
        if (statusPosition == 1 || statusPosition == max.getPosition()) {
            return true;
        } else if (table.getTaskLimit() == 0) {
            return true;
        } else {

            Set<Status> inProgressStatuses = getInProgressStatuses(table, max.getPosition());
            return table.getTaskLimit() > getTaskCount(inProgressStatuses);
        }
    }

    private Integer getTaskCount(Set<Status> statuses) {
        return statuses.stream().mapToInt(value -> value.getTasks().size()).sum();

    }

    private Set<Status> getInProgressStatuses(ScrumTable table, Integer maxPosition) {
        Set<Status> statuses = table.getStatuses();
        return statuses.stream()
                .filter(status -> status.getPosition() > 1 && status.getPosition() != maxPosition)
                .collect(Collectors.toSet());
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
            AppUser appUser = appUserRepository.findById(taskCredentials.getOwner().getId()).get();
            userService.incrementTaskCount(appUser);
            taskCredentials.setOwner(appUser);
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


    public void setFinishTask(UUID taskId) {
        Task task = taskRepository.findById(taskId).get();
        task.setFinished(!task.isFinished());
        taskRepository.save(task);
    }
}
