package com.maksym.productservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(EntityNotFoundException e){
        return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
