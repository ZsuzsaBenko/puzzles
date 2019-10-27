package com.codecool.zsuzsi.puzzlesbackend.exception.customexception;

public class InvalidRegistrationException extends RuntimeException {

    private static final String REGISTRATION_ERROR_MESSAGE =
            "Registration failed: data is incomplete or email already exists.";

    public InvalidRegistrationException() {
        super(REGISTRATION_ERROR_MESSAGE);
    }
}
