package com.codecool.scrumtracker.exception.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ReachMaximumNumberOfTasksException extends Exception {
    public ReachMaximumNumberOfTasksException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }


}
