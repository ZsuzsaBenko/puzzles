package com.codecool.zsuzsi.puzzlesbackend.exception;

import com.codecool.zsuzsi.puzzlesbackend.exception.customexception.ExceptionType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        HttpStatus httpStatus = ExceptionType.valueOf(e.getClass().getSimpleName().toUpperCase()).getHttpStatus();
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse, httpStatus);

    }

}
