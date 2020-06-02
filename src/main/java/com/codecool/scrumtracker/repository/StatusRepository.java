package com.codecool.scrumtracker.repository;

import com.codecool.scrumtracker.model.Status;
import com.codecool.scrumtracker.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StatusRepository extends JpaRepository<Status, UUID> {

    Optional<Status> findByTasksContains(Task task);
}
