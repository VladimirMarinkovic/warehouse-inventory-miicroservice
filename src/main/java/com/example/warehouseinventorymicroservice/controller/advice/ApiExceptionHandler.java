package com.example.warehouseinventorymicroservice.controller.advice;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        final List<String> fieldErrors = ex.getBindingResult()
                                           .getAllErrors()
                                           .stream()
                                           .map(error -> (FieldError) error)
                                           .map(error -> "Invalid param. Field: [" + error.getField() + "], RejectedValue: [" + error.getRejectedValue() + "], Reason: [" + error.getDefaultMessage() + "]")
                                           .collect(Collectors.toList());

        final ErrorDTO error = ErrorDTO.builder()
                                       .httpStatus(HttpStatus.BAD_REQUEST)
                                       .timestamp(LocalDateTime.now())
                                       .message("Validation error")
                                       .subErrors(fieldErrors)
                                       .build();

        return this.buildResponseEntity(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        final ErrorDTO error = ErrorDTO.builder()
                                       .httpStatus(HttpStatus.BAD_REQUEST)
                                       .timestamp(LocalDateTime.now())
                                       .message(ex.getMessage())
                                       .build();
        return this.buildResponseEntity(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<Object> handleIllegalArgumentException(final IllegalArgumentException ex) {
        final ErrorDTO error = ErrorDTO.builder()
                                       .httpStatus(HttpStatus.BAD_REQUEST)
                                       .timestamp(LocalDateTime.now())
                                       .message(ex.getMessage())
                                       .build();

        return this.buildResponseEntity(error);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    ResponseEntity<Object> handleEntityNotFoundException(final EntityNotFoundException ex) {
        final ErrorDTO error = ErrorDTO.builder()
                                       .httpStatus(HttpStatus.NOT_FOUND)
                                       .timestamp(LocalDateTime.now())
                                       .message(ex.getMessage())
                                       .build();

        return this.buildResponseEntity(error);
    }



    private ResponseEntity<Object> buildResponseEntity(final ErrorDTO errorDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(errorDTO, headers, errorDTO.getHttpStatus());
    }

}
