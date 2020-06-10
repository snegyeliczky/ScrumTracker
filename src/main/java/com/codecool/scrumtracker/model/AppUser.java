package com.codecool.scrumtracker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AppUser {

    @GeneratedValue
    @Id
    private UUID id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    @JsonIgnore
    private String password;

    @ElementCollection
    @Builder.Default
    @JsonIgnore
    private List<String> roles = new ArrayList<>();

}
