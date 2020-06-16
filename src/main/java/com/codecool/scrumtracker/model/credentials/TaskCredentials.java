package com.codecool.scrumtracker.model.credentials;

import com.codecool.scrumtracker.model.AppUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskCredentials {
    private UUID statusId;
    private String description;
    private int priority;
    private String title;
    private LocalDateTime deadline;
    private AppUser owner;
}
