package com.codecool.scrumtracker.repository;

import com.codecool.scrumtracker.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StatusRepository extends JpaRepository<Status, UUID> {
}
