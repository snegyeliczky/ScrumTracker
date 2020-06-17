package com.codecool.scrumtracker.exception.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
public class NotAuthoritizedException extends Exception {
    public NotAuthoritizedException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }


}
