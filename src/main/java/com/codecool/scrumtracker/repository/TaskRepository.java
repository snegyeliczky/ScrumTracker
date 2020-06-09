package com.codecool.scrumtracker.repository;

import com.codecool.scrumtracker.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
}
