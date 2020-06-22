package com.codecool.scrumtracker.model.credentials;

import com.codecool.scrumtracker.model.AppUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.time.LocalDate;
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
    @DateTimeFormat(pattern="E MMM dd yyyy HH:mm:ss 'GMT'Z")
    private LocalDateTime deadline;
    private AppUser owner;
    private int position;
}
