package com.codecool.zsuzsi.puzzlesbackend.exception;

import com.codecool.zsuzsi.puzzlesbackend.exception.customexception.ExceptionType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "Szerverhiba történt. Próbáld újra!";

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        try{
            HttpStatus httpStatus = ExceptionType.valueOf(e.getClass().getSimpleName().toUpperCase()).getHttpStatus();
            ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), e.getMessage());
            return new ResponseEntity<>(errorResponse, httpStatus);
        } catch (IllegalArgumentException exception) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),INTERNAL_SERVER_ERROR_MESSAGE);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
