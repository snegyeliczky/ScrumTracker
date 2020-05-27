package com.codecool.scrumtracker.repository;

import com.codecool.scrumtracker.model.ScrumTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ScrumTableRepository extends JpaRepository<ScrumTable, UUID> {


}
