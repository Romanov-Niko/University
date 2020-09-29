package com.foxminded.university.exception;

public class GroupSizeTooLargeException extends RuntimeException {

    public GroupSizeTooLargeException(String errorMessage) {
        super(errorMessage);
    }
}
