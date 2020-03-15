package com.codecool.zsuzsi.puzzlesbackend.exception.customexception;

public class CommentNotFoundException extends RuntimeException {

    private static final String COMMENT_ERROR_MESSAGE = "A hozzászólás nem található.";

    public CommentNotFoundException() {
        super(COMMENT_ERROR_MESSAGE);
    }
}
