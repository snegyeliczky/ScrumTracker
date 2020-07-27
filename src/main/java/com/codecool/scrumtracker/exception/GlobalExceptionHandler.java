package com.codecool.scrumtracker.exception;

import com.codecool.scrumtracker.exception.exceptions.NotAuthorizedException;
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

    @ExceptionHandler({ NotAuthorizedException.class })
    public final ResponseEntity<ApiError> handleNotAuthorizedException(Exception ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.FORBIDDEN;
        NotAuthorizedException unfe = (NotAuthorizedException) ex;

        return handleException(unfe, headers, status, request);

    }

    @ExceptionHandler({ NotProjectOwnerException.class })
    public final ResponseEntity<ApiError> handleNotProjectOwnerException(Exception ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.FORBIDDEN;
        NotProjectOwnerException unfe = (NotProjectOwnerException) ex;

        return handleException(unfe, headers, status, request);

    }

    @ExceptionHandler({ ReachMaximumNumberOfTasksException.class })
    public final ResponseEntity<ApiError> handleReachMaximumNumberOfTasksException(Exception ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.FORBIDDEN;
        ReachMaximumNumberOfTasksException unfe = (ReachMaximumNumberOfTasksException) ex;

        return handleException(unfe, headers, status, request);

    }

    private ResponseEntity<ApiError> handleException(Exception ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return handleExceptionInternal(ex, new ApiError(errors), headers, status, request);
    }


    /** A single place to customize the response body of all Exception types. */
    protected ResponseEntity<ApiError> handleExceptionInternal(Exception ex, ApiError body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }
        body.setStatus(status);

        return new ResponseEntity<>(body, headers, status);
    }
}