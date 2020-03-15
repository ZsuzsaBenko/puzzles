package com.codecool.zsuzsi.puzzlesbackend.exception.customexception;

public class InvalidRegistrationException extends RuntimeException {

    private static final String REGISTRATION_ERROR_MESSAGE =
            "Sikertelen regisztráció: hiányos adatok vagy ezt az e-mail címet már regisztrálták.";

    public InvalidRegistrationException() {
        super(REGISTRATION_ERROR_MESSAGE);
    }
}
