package com.codecool.scrumtracker.exception.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class NotProjectOwnerException extends Exception {
    public NotProjectOwnerException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }


}
