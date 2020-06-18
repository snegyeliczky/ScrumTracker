package com.codecool.scrumtracker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Task {

    @GeneratedValue
    @Id
    private UUID id;

    private String title;

    @Column(columnDefinition="TEXT")
    private String description;

    private Integer priority;

    private boolean archive;

    @OneToOne
    private AppUser author;

    @OneToOne
    private AppUser owner;

    private LocalDateTime deadline;

    private Integer position;



}
