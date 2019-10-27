package com.codecool.zsuzsi.puzzlesbackend.exception.customexception;

public class PuzzleNotFoundException extends RuntimeException {

    private static final String PUZZLE_ERROR_MESSAGE = "The requested puzzle could not be found.";

    public PuzzleNotFoundException() {
        super(PUZZLE_ERROR_MESSAGE);
    }
}
