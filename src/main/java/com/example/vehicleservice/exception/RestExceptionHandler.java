package com.example.vehicleservice.exception;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import jakarta.validation.ConstraintViolationException;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException ex, @NonNull final HttpHeaders headers, @NonNull final HttpStatusCode status, @NonNull final WebRequest request) {

        final var errors = ex.getBindingResult().getFieldErrors().stream().collect(Collectors.toMap(
                FieldError::getField,
                fieldError -> Optional.ofNullable(fieldError.getDefaultMessage()).orElse("Unknown validation error")
        ));

        final var problemDetail = ProblemDetailExt.forStatusDetailAndErrors(status, "Request validation failed.", errors);

        return new ResponseEntity<>(problemDetail, status);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ProblemDetail> handleAuthenticationException(final AuthenticationException ex) {
        final var problemDetail = ProblemDetail.forStatusAndDetail(UNAUTHORIZED, ex.getMessage());

        return new ResponseEntity<>(problemDetail, UNAUTHORIZED);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolationException(final ConstraintViolationException ex, @NonNull final WebRequest request) {
        final var problemDetail = ProblemDetail.forStatusAndDetail(CONFLICT, "Error while processing the request. Please try again.");

        log.warn("Constraint violation error occurred", ex);

        return new ResponseEntity<>(problemDetail, CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGenericException(final Exception ex) {
        final var problemDetail = ProblemDetail.forStatusAndDetail(INTERNAL_SERVER_ERROR, "An unexpected error occurred.");

        log.error("Unexpected error occurred", ex);

        return new ResponseEntity<>(problemDetail, INTERNAL_SERVER_ERROR);
    }

}