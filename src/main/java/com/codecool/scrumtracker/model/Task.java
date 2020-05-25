package com.codecool.scrumtracker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
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

    @OneToOne
    private AppUser author;

    private int position;

    private String description;

    private int businessValue;

}
