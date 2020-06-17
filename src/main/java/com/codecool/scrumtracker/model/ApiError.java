package com.codecool.scrumtracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiError {

    private HttpStatus status;
    private String message;
    private List<String> errors;


    public ApiError(List<String> errors) {
        this.errors = errors;
    }
}
