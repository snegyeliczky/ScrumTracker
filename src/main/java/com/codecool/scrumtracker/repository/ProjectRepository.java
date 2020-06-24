package com.codecool.scrumtracker.repository;

import com.codecool.scrumtracker.model.AppUser;
import com.codecool.scrumtracker.model.Project;
import com.codecool.scrumtracker.model.ScrumTable;
import com.codecool.scrumtracker.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {

    Set<Project> getProjectByAuthor(AppUser author);

    Set<Project> getByAuthorAndArchiveIsFalse(AppUser appUser);

    Set<Project> findProjectByParticipantsContains(AppUser user);

    Set<Project> findProjectsByParticipantsContainsAndAndArchiveIsFalse(AppUser user);

    Set<Project> getProjectByAuthorAndArchiveIsTrue(AppUser author);

    Set<Project> getProjectByParticipantsContainsAndArchiveIsTrue(AppUser appUser);

    Optional<Project> getProjectByTable(ScrumTable table);

    @Query(value = "SELECT project p FROM project " +
            "LEFT JOIN scrum_table st on project.table_id = st.id " +
            "LEFT JOIN scrum_table_statuses sts on st.id = sts.scrum_table_id " +
            "LEFT JOIN status s on sts.statuses_id = s.id " +
            "LEFT JOIN status_tasks t on s.id = t.status_id " +
            "LEFT JOIN task t2 on t.tasks_id = t2.id " +
            "WHERE t2.id = :taskId",
    nativeQuery = true)
    Project getProjectByTaskId(@Param("taskId") UUID id);
}
