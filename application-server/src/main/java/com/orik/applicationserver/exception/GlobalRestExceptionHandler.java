package com.orik.applicationserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class GlobalRestExceptionHandler {
    @ExceptionHandler(NoRequestFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(NoRequestFoundException exception) {
        ErrorResponse requestErrorResponse = new ErrorResponse();
        requestErrorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        requestErrorResponse.setMessage(exception.getMessage());

        ZoneId ukraineZone = ZoneId.of("Europe/Kiev");
        ZonedDateTime time = ZonedDateTime.now(ukraineZone);
        requestErrorResponse.setTime(time);

        return new ResponseEntity<>(requestErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoRoleFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(NoRoleFoundException exception) {
        ErrorResponse roleErrorResponse = new ErrorResponse();
        roleErrorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        roleErrorResponse.setMessage(exception.getMessage());

        ZoneId ukraineZone = ZoneId.of("Europe/Kiev");
        ZonedDateTime time = ZonedDateTime.now(ukraineZone);
        roleErrorResponse.setTime(time);

        return new ResponseEntity<>(roleErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoUserFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(NoUserFoundException exception) {
        ErrorResponse userErrorResponse = new ErrorResponse();
        userErrorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        userErrorResponse.setMessage(exception.getMessage());

        ZoneId ukraineZone = ZoneId.of("Europe/Kiev");
        ZonedDateTime time = ZonedDateTime.now(ukraineZone);
        userErrorResponse.setTime(time);

        return new ResponseEntity<>(userErrorResponse, HttpStatus.NOT_FOUND);
    }
}