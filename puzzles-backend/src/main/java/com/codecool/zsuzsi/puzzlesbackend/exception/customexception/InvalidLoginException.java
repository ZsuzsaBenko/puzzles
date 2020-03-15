package com.codecool.zsuzsi.puzzlesbackend.exception.customexception;

public class InvalidLoginException extends RuntimeException {

    private static final String LOGIN_ERROR_MESSAGE = "Érvénytelen e-mail cím vagy jelszó.";

    public InvalidLoginException() {
        super(LOGIN_ERROR_MESSAGE);
    }
}
