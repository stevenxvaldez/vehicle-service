package com.example.vehicleservice.exception;

import static com.example.vehicleservice.exception.ProblemDetailExt.forStatusDetailAndErrors;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class ValidationException extends ErrorResponseException {

    public ValidationException(final HttpStatus status, final Map<String, String> errors) {
        super(status, forStatusDetailAndErrors(status, "Request validation failed", errors), null);
    }

}