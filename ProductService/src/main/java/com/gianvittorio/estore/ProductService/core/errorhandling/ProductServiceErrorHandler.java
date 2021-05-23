package com.gianvittorio.estore.ProductService.core.errorhandling;

import org.axonframework.commandhandling.CommandExecutionException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.Optional;

@ControllerAdvice
public class ProductServiceErrorHandler {

    @ExceptionHandler(value = {IllegalStateException.class})
    public ResponseEntity<?> handleIllegalStateException(IllegalStateException e, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(new Date(), e.getMessage());

        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<?> handleOtherExceptions(Exception e, WebRequest request) {

        return Optional.of(e.getMessage())
                .map(message -> new ErrorMessage(new Date(), message))
                .map(errorMessage -> new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR))
                .get();
    }

    @ExceptionHandler(value = {CommandExecutionException.class})
    public ResponseEntity<?> handleCommandExecutionException(CommandExecutionException e, WebRequest request) {

        return Optional.of(e.getMessage())
                .map(message -> new ErrorMessage(new Date(), message))
                .map(errorMessage -> new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR))
                .get();
    }
}
