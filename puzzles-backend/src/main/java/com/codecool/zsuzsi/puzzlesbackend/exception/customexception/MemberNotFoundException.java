package com.codecool.zsuzsi.puzzlesbackend.exception.customexception;

public class MemberNotFoundException extends RuntimeException {

    private static final String MEMBER_ERROR_MESSAGE = "A felhasználó nem található.";

    public MemberNotFoundException() {
        super(MEMBER_ERROR_MESSAGE);
    }
}
