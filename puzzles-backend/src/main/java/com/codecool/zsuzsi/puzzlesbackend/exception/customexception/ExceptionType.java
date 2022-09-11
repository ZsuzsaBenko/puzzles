package com.codecool.zsuzsi.puzzlesbackend.exception.customexception;

import org.springframework.http.HttpStatus;

public enum ExceptionType {
    INVALIDLOGINEXCEPTION (HttpStatus.UNAUTHORIZED),
    INVALIDREGISTRATIONEXCEPTION(HttpStatus.BAD_REQUEST),
    PUZZLENOTFOUNDEXCEPTION(HttpStatus.NOT_FOUND),
    MEMBERNOTFOUNDEXCEPTION(HttpStatus.NOT_FOUND),
    COMMENTNOTFOUNDEXCEPTION(HttpStatus.NOT_FOUND),
    SOLUTIONNOTFOUNDEXCEPTION(HttpStatus.NOT_FOUND);

    private final HttpStatus httpStatus;

    ExceptionType(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
