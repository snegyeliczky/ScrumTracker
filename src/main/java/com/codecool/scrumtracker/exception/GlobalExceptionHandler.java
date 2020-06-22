package com.codecool.scrumtracker.exception;

import com.codecool.scrumtracker.exception.exceptions.NotAuthoritizedException;
import com.codecool.scrumtracker.exception.exceptions.NotProjectOwnerException;
import com.codecool.scrumtracker.exception.exceptions.ReachMaximumNumberOfTasksException;
import com.codecool.scrumtracker.model.ApiError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.WebUtils;

import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {
    /** Provides handling for exceptions throughout this service. */
    @ExceptionHandler({ Exception.class })
    public final ResponseEntity<ApiError> handleException(Exception ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();

        if (ex instanceof NotAuthoritizedException) {

            HttpStatus status = HttpStatus.NOT_FOUND;
            NotAuthoritizedException unfe = (NotAuthoritizedException) ex;

            return handleNotAuthoritizedException(unfe, headers, status, request);
        } else if (ex instanceof NotProjectOwnerException) {

            HttpStatus status = HttpStatus.NOT_FOUND;
            NotProjectOwnerException unfe = (NotProjectOwnerException) ex;

            return handleNotProjectOwnerException(unfe, headers, status, request);
        } else if (ex instanceof ReachMaximumNumberOfTasksException) {

            HttpStatus status = HttpStatus.NOT_FOUND;
            ReachMaximumNumberOfTasksException unfe = (ReachMaximumNumberOfTasksException) ex;

            return handleReachMaximumNumberOfTasksException(unfe, headers, status, request);
        }
        HttpStatus status = HttpStatus.NOT_FOUND;
        NotAuthoritizedException unfe = (NotAuthoritizedException) ex;
        return handleNotAuthoritizedException(unfe, headers, status, request);
    }

    private ResponseEntity<ApiError> handleNotProjectOwnerException(NotProjectOwnerException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return handleExceptionInternal(ex, new ApiError(errors), headers, status, request);
    }

    protected ResponseEntity<ApiError> handleNotAuthoritizedException(NotAuthoritizedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return handleExceptionInternal(ex, new ApiError(errors), headers, status, request);
    }

    protected ResponseEntity<ApiError> handleReachMaximumNumberOfTasksException(ReachMaximumNumberOfTasksException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return handleExceptionInternal(ex, new ApiError(errors), headers, status, request);
    }


    /** A single place to customize the response body of all Exception types. */
    protected ResponseEntity<ApiError> handleExceptionInternal(Exception ex, ApiError body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }

        return new ResponseEntity<>(body, headers, status);
    }
}