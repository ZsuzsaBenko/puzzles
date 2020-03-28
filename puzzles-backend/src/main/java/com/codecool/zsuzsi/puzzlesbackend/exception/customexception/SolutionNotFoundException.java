package com.codecool.zsuzsi.puzzlesbackend.exception.customexception;

public class SolutionNotFoundException extends RuntimeException {
    private static final String SOLUTION_ERROR_MESSAGE = "A megfejtés nem található.";

    public SolutionNotFoundException() {
        super(SOLUTION_ERROR_MESSAGE);
    }
}
