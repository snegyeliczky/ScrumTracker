package com.codecool.scrumtracker.repository;

import com.codecool.scrumtracker.model.ScrumTable;
import com.codecool.scrumtracker.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ScrumTableRepository extends JpaRepository<ScrumTable, UUID> {

    Optional<ScrumTable> findByStatusesContaining(Status status);


}
