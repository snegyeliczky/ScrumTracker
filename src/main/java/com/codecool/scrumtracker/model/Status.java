package com.codecool.scrumtracker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Status {

    @GeneratedValue
    @Id
    private UUID id;

    private String statusName;

    private int position;

    @OneToMany(cascade= CascadeType.ALL)
    private Set<Task> tasks;
}
