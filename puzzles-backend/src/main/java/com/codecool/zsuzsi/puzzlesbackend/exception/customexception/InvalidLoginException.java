package com.codecool.zsuzsi.puzzlesbackend.exception.customexception;

public class InvalidLoginException extends RuntimeException {

    private static final String LOGIN_ERROR_MESSAGE = "Invalid email / password supplied.";

    public InvalidLoginException() {
        super(LOGIN_ERROR_MESSAGE);
    }
}
