package com.codecool.scrumtracker.repository;

import com.codecool.scrumtracker.model.AppUser;
import com.codecool.scrumtracker.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {

    Set<Project> getProjectByAuthor(AppUser author);
    Set<Project> getProjectByParticipantsContaining(AppUser user);
}
