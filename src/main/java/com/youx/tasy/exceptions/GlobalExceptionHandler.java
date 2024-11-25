package com.youx.tasy.exceptions;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErroRespose> handleBusinessException(BusinessException ex) {
        var erroRespose = new ErroRespose("Business Exception", ex.getMessage());
        return new ResponseEntity<>(erroRespose, ex.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErroRespose>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ErroRespose> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ErroRespose(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List<ErroRespose>> handleConstraintViolation(ConstraintViolationException ex) {
        List<ErroRespose> errors = ex.getConstraintViolations()
                .stream()
                .map(violation -> new ErroRespose(
                        violation.getPropertyPath().toString(), violation.getMessage()))
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
