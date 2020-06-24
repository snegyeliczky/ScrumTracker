package com.codecool.scrumtracker.exception.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class NotAuthorizedException extends Exception {
    public NotAuthorizedException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }


}
