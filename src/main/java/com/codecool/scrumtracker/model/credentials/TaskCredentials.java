package com.codecool.scrumtracker.model.credentials;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskCredentials {
    private UUID statusId;
    private String description;
    private String title;
}