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
public class ScrumTable {

    @GeneratedValue
    @Id
    private UUID id;

    @OneToMany(cascade= CascadeType.ALL)
    private Set<Status> statuses;
}
