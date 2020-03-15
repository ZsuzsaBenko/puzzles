package com.codecool.zsuzsi.puzzlesbackend.exception.customexception;

public class PuzzleNotFoundException extends RuntimeException {

    private static final String PUZZLE_ERROR_MESSAGE = "A fejtörő nem található.";

    public PuzzleNotFoundException() {
        super(PUZZLE_ERROR_MESSAGE);
    }
}
